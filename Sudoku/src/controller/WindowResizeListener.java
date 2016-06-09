package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import view.View;


public class WindowResizeListener implements ChangeListener<Number> {
	
	private View view;
	private int k;
	
	public WindowResizeListener(View view, int k) {
		this.view = view;
		this.k = k;
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		int val = view.newSize();
		if (val > 0) {
			view.scaleSudoku((int)val, k);
		}
	}

}
