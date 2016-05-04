package view;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class numberFieldController<T> implements EventHandler<MouseEvent> {

	Main view;
	int number;
	
	public numberFieldController(Main main, int number) {
		this.view = main;
		this.number = number;
	}

	@Override
	public void handle(MouseEvent event) {
		view.inputNumber(number);
	}

}
