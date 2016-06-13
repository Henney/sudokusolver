package model.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import model.Grid;
import model.Writer;

public class WriterTest {
	
	public void write(String loc) throws FileNotFoundException, IOException {
		String[] puz = { "didoku1.txt", "sudoku1.txt", "sudoku2.txt", "tetradoku1.txt", "pentadoku1.txt",
				"hexadoku1.txt", "heptadoku1.txt", "octadoku1.txt", "enneadoku1.txt", "decadoku1.txt" };
		
		for (String s : puz) {
			Grid g = new Grid(new FileReader("puzzles/" + s));
			assertNotNull(g);			
			File f = Writer.writeToFile(g, loc);
			assertNotNull(f);			
			Grid g1_ = new Grid(new FileReader(f.getPath()));
			assertTrue(g.equals(g1_));
		}
		
	}
	
	@Test
	public void writeSpecificLocation() throws FileNotFoundException, IOException {
		String loc = "test/puzzles/";
		
		File p = new File(loc);
		if (p.exists()) {
			assertTrue(deleteDir(p));
		}
		assertFalse(p.exists());

		write(loc);
	}

	public boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

}
