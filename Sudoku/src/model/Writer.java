package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Writer {
	
	public static void main(String[] args) {
		int amount = 1;
		for (int i = 0; i < amount; i++) {
			Grid g = PuzzleGenerator.generate(6);
			writeToFile(g);
		}
	}

	public static boolean writeToFile(Grid g) {
		int k = g.k();
		
		String prefix = "";
		switch(k) {
		case 2: prefix = "di"; break;
		case 3: prefix = "su"; break;
		case 4: prefix = "tetra"; break;
		case 5: prefix = "penta"; break;
		case 6: prefix = "hexa"; break;
		case 7: prefix = "hepta"; break;
		case 8: prefix = "octa"; break;
		case 9: prefix = "ennea"; break;
		case 10: prefix = "deca"; break;
		}
		prefix += "doku";
		String path = "puzzles/" + prefix + "s/";
		
		String name;
		File f;
		int num = 0;
		do {
			num++;
			name = prefix + num;
			f = new File(path, name + ".txt");
		} while (f.exists());
				
		try {
			f.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(k + "\n");
		int n = g.size();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int value = g.get(i,j);
				sb.append((value > 0 ? value : ".") + ";");
			}
			sb.append("\n");
		}
		
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream(f.getAbsolutePath()), "utf-8"))) {
		   writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
