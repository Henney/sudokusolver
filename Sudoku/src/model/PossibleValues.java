package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PossibleValues {
	private boolean[] possible;
	private int numberPossible;
	
	public PossibleValues(int maxValue) {
		possible = new boolean[maxValue+1];
		Arrays.fill(possible, true);
		numberPossible = maxValue;
	}
	
	public PossibleValues(PossibleValues other) {
		possible = Arrays.copyOf(other.possible, other.possible.length);
		numberPossible = other.numberPossible;
	}
	
	public void merge(PossibleValues other) {
		numberPossible = 0;
		for (int i = 0; i < other.maxValue(); i++) {
			possible[i] &= other.get(i);
			
			if (possible[i]) {
				numberPossible++;
			}
		}
	}
	
	public int possible() {
		return numberPossible;
	}
	
	public int maxValue() {
		return possible.length;
	}
	
	public boolean set(int value, boolean p) {
		boolean old = possible[value];
		
		possible[value] = p;
		
		if (!old && p) {
			numberPossible++;
			return true;
		} else if (old && !p) {
			numberPossible--;
			return true;
		}
		
		return false;
	}
	
	public boolean get(int value) {
		return possible[value];
	}
	
	public int nextAfter(int value) {
		for (int i = value+1; i < possible.length; i++) {
			if (possible[i]) {
				return i;
			}
		}
		
		return 0;
	}
	
	public Set<Integer> toSet() {
		HashSet<Integer> s = new HashSet<Integer>();
		
		for (int i = 1; i < possible.length; i++) {
			if (possible[i]) {
				s.add(i);
			}
		}
		
		return s;
	}
	
	// TODO: next after value
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{ ");
		
		for (int value = 0; value < possible.length; value++) {
			if (possible[value]) {
				sb.append("" + value + " ");
			}
		}
		
		sb.append("}");
		
		return sb.toString();
	}
	
}