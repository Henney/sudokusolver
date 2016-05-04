package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import view.View;


public class WindowResizeListener implements ChangeListener<Number> {
	

	public enum Property {
		Width, Height;
	}
	
	private View view;
	private int k;
	private Property prop;
	
	public WindowResizeListener(View view, int k, Property prop) {
		this.view = view;
		this.k = k;	
		this.prop = prop;
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		double otherPropSize = -1;
		if (prop == Property.Width) {
			otherPropSize = view.getRootLayout().getHeight();
		} else if (prop == Property.Height) {

			otherPropSize = view.getRootLayout().getHeight();
		}
		
		int propSize = (int) ((Double)newValue).doubleValue();
		if (propSize > otherPropSize) return;
		view.scaleSudoku(propSize, k);
	}

}
