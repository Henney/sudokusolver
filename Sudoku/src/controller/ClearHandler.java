package controller;

import javafx.scene.input.MouseEvent;
import model.UserGrid;
import view.View;

public class ClearHandler<T> extends ButtonHandler<MouseEvent> {

	public ClearHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent arg0) {
		view.setAndDisplayGrid(new UserGrid(view.getGrid().k()));
	}

}
