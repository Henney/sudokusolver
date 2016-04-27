package model.util;

public class IntLinkedList {

	private Node head = new Node(-1, null, null);

	public boolean isEmpty() {
		return this.head.next == null;
	}

	public Node push(int value) {
		Node n = new Node(value, null, null);
		push(n);
		return this.head.next;
	}

	public void push(Node n) {
		n.next = this.head.next;
		n.prev = this.head;
		this.head.next = n;
	}

	public int poll() {
		assert !this.isEmpty();

		Node ret = head.next;
		head.next = ret.next;

		if (head.next != null) {
			head.next.prev = null;
		}

		return ret.value;
	}
}
