package controller;

import javafx.scene.input.MouseEvent;
import view.View;
import view.View.Method;

public class SolveHandler<T> extends ButtonHandler<MouseEvent> {
	
	public SolveHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent e) {
		view.solveSudoku(Method.Tactic);
	}

}
