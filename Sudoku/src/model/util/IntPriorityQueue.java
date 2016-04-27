package model.util;

public class IntPriorityQueue {

	private IntLinkedList[] queue;
	private int next;
	private Node[] nodes;
	
	public IntPriorityQueue(int maxValue, int maxPrio) {
		this.queue = new IntLinkedList[maxPrio+1];
		
		for (int i = 0; i <= maxPrio; i++) {
			this.queue[i] = new IntLinkedList();
		}
		
		this.next = 0;
		
		this.nodes = new Node[maxValue+1];
	}
	
	public void insert(int value, int prio) {
		nodes[value] = this.queue[prio].push(value);
		
		if (prio < next) {
			next = prio;
		}
	}
	
	public int extractMin() {
		while (this.queue[this.next].isEmpty()) {
			this.next++;
		}
		
		int val = this.queue[this.next].poll();
		nodes[val] = null;
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
}
