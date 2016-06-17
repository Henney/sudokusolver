package model.util;

import java.util.concurrent.Callable;

import model.Solver;
import model.TimeoutError;

public class SolvableCallable implements Callable<TimeoutBoolean> {
	
	private Solver s;
	private int timeout;
	
	public SolvableCallable(Solver s, int timeout) {
		this.s = s;
		this.timeout = timeout;
	}
	
	@Override
	public TimeoutBoolean call() throws Exception {
		boolean b;
		try {
			b = s.solvableWithTimeout(timeout);
		} catch (TimeoutError e) {
			return TimeoutBoolean.TIMEOUT;
		}
		
		return TimeoutBoolean.fromBoolean(b);
	}
}
