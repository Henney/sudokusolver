package model.util;

public class Pair<A, B> {

	public final A fst;
	public final B snd;
	
	public Pair(A a, B b) {
		fst = a;
		snd = b;
	}
	
	public String toString() {
		return "{ " + fst.toString() + " : " + snd.toString() + " }"; 
	}
}
