package model.util;

public class Node {
	int value;
	Node next;
	Node prev;
	
	public Node(int value) {
		this.value = value;
	}
	
	public void remove() {
		if (prev != null) {
			prev.next = next;
		}
		
		if (next != null) {
			next.prev = prev;
		}
	}
}
