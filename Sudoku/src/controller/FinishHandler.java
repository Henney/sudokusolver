package controller;

import javafx.scene.input.MouseEvent;
import view.View;

public class FinishHandler<T> extends ButtonHandler<MouseEvent> {

	public FinishHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent event) {
		view.getStage().getScene().lookup("#SpeedSlider").disableProperty().set(true);
		view.setSolveSpeed(0);
	}

}
