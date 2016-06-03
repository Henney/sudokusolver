package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Writer {
	
	public static final String DEFAULT_PATH = "puzzles/";

	public static File writeToFile(Grid g) {
		return writeToFile(g, DEFAULT_PATH);
	}
	
	public static File writeToFile(Grid g, String loc) {
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
		String path = loc + prefix + "s/";
		
		String name;
		File f;
		int num = 0;
		do {
			num++;
			name = prefix + num;
			f = new File(path, name + ".txt");
		} while (f.exists());
		
		if (!f.getParentFile().exists() && !f.getParentFile().mkdirs()) {
			return null;
		}
				
		try {
			f.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
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
		   writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
		
		return f;
	}
}
