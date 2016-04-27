package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class Parser {

	public static final String SEPARATOR = ";";
	public static final String EMPTY = ".";
	
	// TODO: throw exceptions for wrong input

	public static int[] parseGrid(Reader input) throws IOException {

		BufferedReader in = new BufferedReader(input);

		int k = Integer.parseInt(in.readLine());

		int n = k * k;

		int[] grid = new int[n * n];

		int currentField = 0;

		for (int i = 0; i < n; i++) {
			String[] fields = in.readLine().split(SEPARATOR);
			if (fields.length != n) {
				System.err.println("Error: Not enough columns in row: " + (i + 1));
			}

			for (int j = 0; j < fields.length; j++) {
				if (fields[j].equals(EMPTY)) {
					grid[currentField] = 0;
				} else
					try {
						int number = Integer.parseInt(fields[j]);
						grid[currentField] = number;
					} catch (NumberFormatException e) {
						// TODO: Handle error
						System.err.println("Error: Illegal character in grid: " + fields[j]);
					}

				currentField++;
			}
		}

		return grid;
	}

}
