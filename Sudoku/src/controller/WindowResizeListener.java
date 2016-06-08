package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
		int val = view.newSize();
		if (val > 0) {
			view.scaleSudoku((int)val, k);
		}
	}

}
