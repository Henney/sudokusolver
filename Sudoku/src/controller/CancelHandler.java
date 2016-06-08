package controller;

import javafx.scene.input.MouseEvent;
import view.View;

public class CancelHandler<T> extends ButtonHandler<MouseEvent> {
	
	public CancelHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent e) {
		view.cancelSolveTask();
	}

}
