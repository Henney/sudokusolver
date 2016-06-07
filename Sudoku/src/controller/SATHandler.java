package controller;

import javafx.scene.input.MouseEvent;
import view.View;
import view.View.Method;

public class SATHandler<T> extends ButtonHandler<MouseEvent> {
	
	public SATHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent e) {
		view.solveSudoku(Method.SAT);
	}
}
