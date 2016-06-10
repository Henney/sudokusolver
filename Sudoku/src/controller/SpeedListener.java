package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import view.View;

public class SpeedListener<T> implements ChangeListener<Number> {

	private View view;
	public static final int MAX_DELAY = 1000;
	
	public SpeedListener(View view) {
		this.view = view;
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		view.calcSolveSpeed(newValue.doubleValue());
	}

}
