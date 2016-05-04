package controller;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import view.Main;

public class SudokuFieldController<T> implements EventHandler<MouseEvent> {
	
	Main view;
	Button field;

	public SudokuFieldController(Main main, Button b) {
		this.view = main;
		this.field = b;
	}

	@Override
	public void handle(MouseEvent event) {
		view.setSelectedField(field);
	}

}
