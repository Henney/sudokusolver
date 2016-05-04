package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import view.View;

public class numberFieldController<T> implements EventHandler<MouseEvent> {

	View view;
	int number;
	
	public numberFieldController(View main, int number) {
		this.view = main;
		this.number = number;
	}

	@Override
	public void handle(MouseEvent event) {
		view.inputNumber(number);
	}

}
