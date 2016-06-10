package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import model.util.Pair;

public class Parser {

	public static final String SEPARATOR = ";";
	public static final String EMPTY = ".";

	public static Pair<int[], Integer> parseGrid(Reader input) throws IOException {

		BufferedReader in = new BufferedReader(input);

		int k;
		try {
			k = Integer.parseInt(in.readLine());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("First line in file should be the number k.");
		}

		int n = k * k;

		int[] grid = new int[n * n];

		int currentField = 0;

		for (int i = 0; i < n; i++) {
			String[] fields = in.readLine().split(SEPARATOR);
			if (fields.length != n) {
				throw new IllegalArgumentException("Not enough columns in row: " + (i + 1));
			}

			for (int j = 0; j < fields.length; j++) {
				if (fields[j].equals(EMPTY)) {
					grid[currentField] = 0;
				} else
					try {
						int number = Integer.parseInt(fields[j]);
						grid[currentField] = number;
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("Illegal character in grid: " + fields[j]);
					}

				currentField++;
			}
		}

		return new Pair<int[], Integer>(grid, k);
	}

}
