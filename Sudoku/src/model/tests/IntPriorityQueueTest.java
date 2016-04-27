package model.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.util.IntPriorityQueue;

public class IntPriorityQueueTest {

	@Test
	public void smallTest() {
		IntPriorityQueue q = new IntPriorityQueue(105, 10);
		
		q.insert(101, 1);
		q.insert(102, 2);
		q.insert(105, 5);
		q.insert(103, 3);
		
		assertEquals(101, q.extractMin());
		assertEquals(102, q.extractMin());
		
		q.changePrio(105, 0);
		
		assertEquals(105, q.extractMin());
		assertEquals(103, q.extractMin());
	}
	
	@Test
	public void k9() {
		int k = 3;
		
		IntPriorityQueue q = new IntPriorityQueue(k*k, k);
		
		for (int i = 1; i <= k*k; i++) {
			for (int prio = 1; prio <= k; prio++) {
				q.insert(i, prio);
			}
		}
		
		for (int prio = 1; prio <= k; prio++) {
			for (int i = k*k; i > 0; i--) {
				int min = q.extractMin();
				
				assertEquals(i, min);
			}
		}
	}
	
}
