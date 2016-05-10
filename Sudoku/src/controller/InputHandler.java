package controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;
import view.SudokuGridPane;
import view.View;

public class InputHandler implements EventHandler<KeyEvent> {
	
	SudokuGridPane sudoku;
	
	public InputHandler(SudokuGridPane sudoku) {
		this.sudoku = sudoku;
	}

	@Override
	public void handle(KeyEvent e) {
		if (sudoku.getSelectedField() != null) {
			KeyCode kc = e.getCode();
			if (kc.isDigitKey()) {
				sudoku.inputNumberNoDelete(kc.getName().charAt(0)-'0');
			} else if (kc == KeyCode.DELETE || kc == KeyCode.BACK_SPACE) {
				sudoku.clearSelectedField();
			} else if (kc.isArrowKey()) {
				switch (kc) {
				case UP: 
				case KP_UP: sudoku.moveSelectedField(0, -1); break;
				case DOWN: 
				case KP_DOWN: sudoku.moveSelectedField(0, 1); break;
				case LEFT: 
				case KP_LEFT: sudoku.moveSelectedField(-1, 0); break;
				case RIGHT: 
				case KP_RIGHT: sudoku.moveSelectedField(1, 0); break;
				default: break;
				}
			} 

		}
	}


}
