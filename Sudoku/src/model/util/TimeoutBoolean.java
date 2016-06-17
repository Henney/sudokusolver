package model.util;

public enum TimeoutBoolean {
	TRUE,
	FALSE,
	TIMEOUT;
	
	public static TimeoutBoolean fromBoolean(boolean b) {
		if (b) {
			return TRUE;
		} else {
			return FALSE;
		}
	}
	
	public TimeoutBoolean or(TimeoutBoolean other) {
		switch (this) {
		case TRUE: return TRUE;
		case FALSE: return fromBoolean(other == TRUE);
		case TIMEOUT: return other;
		default: throw new IllegalStateException("TimeoutBoolean: Internal error.");
		}
	}
}
