package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class SATSolver extends Solver {

	public Process process;
	private final int k;
	private final int n;
	private final int numberOfVariables;
	private final int numberOfClauses;
	
	private class Variable {
		final int row, col, val;
		
		public Variable(int r, int c, int v) {
			row = r;
			col = c;
			val = v;
		}
	}
	
	private int counter;
	private Variable[] variables;
	private int[][][] counters;
	
	public SATSolver(Grid g) {
		super(g);
		
		if (g == null) {
			numberOfClauses = -1;
			numberOfVariables = -1;
			k = -1;
			n = -1;
			return;
		}
		
		k = g.k();
		n = g.size();
		
		final int k4 = k*k*k*k;
		final int k6 = k4*k*k;
		
		numberOfVariables = n*n*n;
		final int numberOfRuleClauses = n*(k6 - k4 + 3*n*n*n - 3*n*n + 6*n) / 2;
		
		int givenVariables = 0;
		
		for (int i = 0; i < grid.numberOfFields(); i++) {
			if (grid.get(i) != 0) {
				givenVariables++;
			}
		}
		
		numberOfClauses = numberOfRuleClauses + givenVariables;
		
		variables = new Variable[numberOfVariables+1];
		counters = new int[n+1][n+1][n+1];
	}
	
	@Override
	public Grid solve() {
		try {
			return solveHelper();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			return null; // Probably timed out
		}
	}
	
	@Override
	public void cancel() {
		super.cancel();
		
		if (process != null) {
			process.destroyForcibly();
			process = null;
		}
		
	}

	public Grid solveHelper() throws IOException {			
		if (!run) return null;

		// -m option 4096 or higher?
		
		ProcessBuilder pb = new ProcessBuilder("plingeling");
		process = pb.start();
		
		OutputStream out = process.getOutputStream();
		OutputStreamWriter w = new OutputStreamWriter(out, "UTF-8");

		w.write("p cnf " + numberOfVariables + " " + numberOfClauses + "\n");
		
		generateRules(w);
		if (!run) return null;
		
		generateGiven(w);
		if (!run) return null;
		
		w.close();
		
		InputStream in = process.getInputStream();
		BufferedReader b = new BufferedReader(new InputStreamReader(in));

		String line;
		Grid ret = new Grid(k); // TODO: new grid?
		
		while (run && (line = b.readLine()) != null) {
			if (timeout != 0 && System.currentTimeMillis() - start > timeout) {
				return null;
			}
			
			if (line.startsWith("s")) {
				if (!line.startsWith("s SATISFIABLE")) {
					return null;
				}
			}
			
			if (line.startsWith("v")) {
				line = line.substring(2);
				
				for (String v : line.split(" ")) {
					final int c = Integer.parseInt(v);
					
					if (c > 0) {
						Variable var = variables[c];
						ret.set(var.row-1, var.col-1, var.val);
					}
				}
			}
		}
		
		return ret;
	}

	public void generateRules(Writer w) throws IOException {
		if (!run) return;

		// Necessary constraints
		atLeastOneInEachField(w);
		if (!run) return;
		onceInRows(w);
		if (!run) return;
		onceInCols(w);
		if (!run) return;
		onceInBoxes(w);
		if (!run) return;

		// Redundant but helpful constraints
		atMostOneInEach(w);
		if (!run) return;
		atLeastOnceInEachRow(w);
		if (!run) return;
		atLeastOnceInEachCol(w);
		// atLeastOnceInEachBox is not helpful
	}

	public void generateGiven(Writer w) throws IOException {
		for (int x = 0; x < grid.size(); x++) {
			for (int y = 0; y < grid.size(); y++) {
				int i = grid.get(x, y);

				if (i != 0) {
					w.write(makeConstant(x + 1, y + 1, i) + " 0\n");
				}
			}
		}
	}

	private int makeConstant(int row, int col, int val) {
		if (counters[row][col][val] == 0) {
			counter++;
			counters[row][col][val] = counter;
			variables[counter] = new Variable(row, col, val);
		}
		
		return counters[row][col][val];
	}

	private void atLeastOneInEachField(Writer w) throws IOException {
		for (int x = 1; x <= n; x++) {
			for (int y = 1; y <= n; y++) {
				for (int z = 1; z <= n; z++) {
					w.write(makeConstant(x, y, z) + " ");
				}
				w.write(" 0\n");
			}
		}
	}

	private void onceInRows(Writer w) throws IOException {
		for (int y = 1; y <= n; y++) {
			for (int z = 1; z <= n; z++) {
				for (int x = 1; x <= n - 1; x++) {
					for (int i = x + 1; i <= n; i++) {
						w.write("-" + makeConstant(x, y, z) + " -" + makeConstant(i, y, z) + " 0\n");
					}
				}
			}
		}
	}

	private void onceInCols(Writer w) throws IOException {
		for (int x = 1; x <= n; x++) {
			for (int z = 1; z <= n; z++) {
				for (int y = 1; y <= n - 1; y++) {
					for (int i = y + 1; i <= n; i++) {
						w.write("-" + makeConstant(x, y, z) + " -" + makeConstant(x, i, z) + " 0\n");
					}
				}
			}
		}
	}

	private void onceInBoxes(Writer w) throws IOException {
		onceInBoxes1(w);
		onceInBoxes2(w);
	}

	private void onceInBoxes1(Writer w) throws IOException {
		for (int z = 1; z <= n; z++) {
			for (int i = 0; i < k; i++) {
				for (int j = 0; j < k; j++) {
					for (int x = 1; x <= k; x++) {
						for (int y = 1; y <= k; y++) {
							for (int m = y + 1; m <= k; m++) {
								int a = makeConstant(k * i + x, k * j + y, z);
								int b = makeConstant(k * i + x, k * j + m, z);
								w.write("-" + a + " -" + b + " 0\n");
							}
						}
					}
				}
			}
		}
	}

	private void onceInBoxes2(Writer w) throws IOException {
		for (int z = 1; z <= n; z++) {
			for (int i = 0; i < k; i++) {
				for (int j = 0; j < k; j++) {
					for (int x = 1; x <= k; x++) {
						for (int y = 1; y <= k; y++) {
							for (int m = x + 1; m <= k; m++) {
								for (int l = 1; l <= k; l++) {
									int a = makeConstant(k * i + x, k * j + y, z);
									int b = makeConstant(k * i + m, k * j + l, z);
									w.write("-" + a + " -" + b + " 0\n");
								}
							}
						}
					}
				}
			}
		}
	}

	private void atMostOneInEach(Writer w) throws IOException {
		for (int x = 1; x <= n; x++) {
			for (int y = 1; y <= n; y++) {
				for (int z = 1; z <= n - 1; z++) {
					for (int i = z + 1; i <= n; i++) {
						w.write("-" + makeConstant(x, y, z) + " -" + makeConstant(x, y, i) + " 0\n");
					}
				}
			}
		}
	}

	private void atLeastOnceInEachRow(Writer w) throws IOException {
		for (int y = 1; y <= n; y++) {
			for (int z = 1; z <= n; z++) {
				for (int x = 1; x <= n; x++) {
					w.write(makeConstant(x, y, z) + " ");
				}
				w.write("0\n");
			}
		}
	}

	private void atLeastOnceInEachCol(Writer w) throws IOException {
		for (int x = 1; x <= n; x++) {
			for (int z = 1; z <= n; z++) {
				for (int y = 1; y <= n; y++) {
					w.write(makeConstant(x, y, z) + " ");
				}
				w.write("0\n");
			}
		}
	}

}
