package model.util;

public class IntLinkedList {

	private Node head = new Node(-42);

	public boolean isEmpty() {
		return head.next == null;
	}

	public Node push(int value) {
		Node n = new Node(value);
		push(n);
		return n;
	}

	public void push(Node n) {
		if (head.next != null) {
			head.next.prev = n;
		}
		
		n.next = head.next;
		n.prev = head;
		head.next = n;
	}

	public int poll() {
		Node ret = head.next;
		head.next = ret.next;
		
		if (head.next != null) {
			head.next.prev = head;
		}

		return ret.value;
	}
}
