package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import view.SudokuButton;
import view.SudokuGridPane;

public class SudokuFieldController<T> implements EventHandler<MouseEvent> {
	
	SudokuGridPane sudoku;
	SudokuButton field;

	public SudokuFieldController(SudokuGridPane sudoku, SudokuButton b) {
		this.sudoku = sudoku;
		this.field = b;
	}

	@Override
	public void handle(MouseEvent event) {
		sudoku.setSelectedField(field);
	}

}
