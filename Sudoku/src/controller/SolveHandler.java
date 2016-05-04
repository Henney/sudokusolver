package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import view.View;

public class SolveHandler<T> implements EventHandler<MouseEvent> {
	
	private View view;
	
	public SolveHandler(View view) {
		this.view = view;
	}

	@Override
	public void handle(MouseEvent e) {
		view.solveSudoku();
	}

}
