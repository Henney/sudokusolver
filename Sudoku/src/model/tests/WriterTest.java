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
		String n1 = "sudoku1.txt";
		String n2 = "tetradoku1.txt";
		
		Grid g1 = new Grid(new FileReader("puzzles/" + n1));
		Grid g2 = new Grid(new FileReader("puzzles/" + n2));

		assertNotNull(g1);
		assertNotNull(g2);
		
		File f1 = Writer.writeToFile(g1, loc);
		File f2 = Writer.writeToFile(g2, loc);
		

		assertNotNull(f1);
		assertNotNull(f2);
		
		Grid g1_ = new Grid(new FileReader(f1.getPath()));
		Grid g2_ = new Grid(new FileReader(f2.getPath()));

		assertTrue(g1.equals(g1_));
		assertTrue(g2.equals(g2_));
		
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
