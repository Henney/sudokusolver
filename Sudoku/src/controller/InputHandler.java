package controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import view.View;

public class InputHandler implements EventHandler<KeyEvent> {
	
	View view;
	
	public InputHandler(View view) {
		this.view = view;
	}

	@Override
	public void handle(KeyEvent e) {
		if (view.getSelectedField() != null) {
			KeyCode kc = e.getCode();
			if (kc.isDigitKey()) {
				view.inputNumberNoDelete(kc.getName().charAt(0)-'0');
			} else if (kc == KeyCode.DELETE || kc == KeyCode.BACK_SPACE) {
				view.clearSelectedField();
			}
		}
	}


}
