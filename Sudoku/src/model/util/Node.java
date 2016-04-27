package model.util;

public class Node {
	int value;
	Node next;
	Node prev;
	
	public Node(int value, Node next, Node prev) {
		this.value = value;
		this.next = next;
		this.prev = prev;
	}
	
	public void remove() {
		if (this.prev != null) {
			this.prev.next = this.next;
		}
		
		if (this.next != null) {
			this.next.prev = this.prev;
		}
	}
}
