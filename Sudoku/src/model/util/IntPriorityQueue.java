package model.util;

public class IntPriorityQueue {

	private IntLinkedList[] queue;
	private Node[] nodes;
	private int next;
	private int size;

	public IntPriorityQueue(int maxValue, int maxPrio) {
		queue = new IntLinkedList[maxPrio + 1];
		nodes = new Node[maxValue + 1];

		for (int i = 0; i <= maxPrio; i++) {
			queue[i] = new IntLinkedList();
		}

		next = 0;
		size = 0;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}

	public void insert(int value, int prio) {
		nodes[value] = queue[prio].push(value);

		if (prio < next) {
			next = prio;
		}
		
		size++;
	}

	public int extractMin() {
		assert !isEmpty();
		
		while (next < queue.length && queue[next].isEmpty()) {
			next++;
		}

		int val = this.queue[next].poll();
		nodes[val] = null;
		size--;
		
		return val;
	}

	public void changePrio(int value, int newPrio) {
		Node n = nodes[value];
		n.remove();
		
		queue[newPrio].push(n);

		if (newPrio < next) {
			next = newPrio;
		}
	}
	
	public IntLinkedList valuesWithPrio(int prio) {
		return queue[prio];
	}
}
