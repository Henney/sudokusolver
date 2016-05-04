package controller;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import view.View;

public class SudokuFieldController<T> implements EventHandler<MouseEvent> {
	
	View view;
	Button field;

	public SudokuFieldController(View main, Button b) {
		this.view = main;
		this.field = b;
	}

	@Override
	public void handle(MouseEvent event) {
		view.setSelectedField(field);
	}

}
