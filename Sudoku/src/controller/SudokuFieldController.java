package controller;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import view.SudokuButton;
import view.View;

public class SudokuFieldController<T> implements EventHandler<MouseEvent> {
	
	View view;
	SudokuButton field;

	public SudokuFieldController(View main, SudokuButton b) {
		this.view = main;
		this.field = b;
	}

	@Override
	public void handle(MouseEvent event) {
		view.setSelectedField(field);
	}

}
