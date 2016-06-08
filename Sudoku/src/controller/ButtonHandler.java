package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import view.View;

public abstract class ButtonHandler<T> implements EventHandler<MouseEvent> {
	
	protected View view;
	
	public ButtonHandler(View view) {
		this.view = view;
	}

}
