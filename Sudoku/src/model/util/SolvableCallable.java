package model.util;

import java.util.concurrent.Callable;

import model.Solver;

public class SolvableCallable implements Callable<Boolean> {
	
	private Solver s;
	private int timeout;
	
	public SolvableCallable(Solver s, int timeout) {
		this.s = s;
		this.timeout = timeout;
	}
	
	@Override
	public Boolean call() throws Exception {
//		return s.solvable();
		return s.solvableWithTimeout(timeout);
	}
}
