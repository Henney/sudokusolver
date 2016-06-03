package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class SpeedListener<T> implements ChangeListener<Number> {

	private View view;
	public static final int MAX_DELAY = 1000;
	
	public SpeedListener(View view) {
		this.view = view;
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		view.setSolveSpeed(MAX_DELAY-(newValue.intValue()*(MAX_DELAY/100)));
	}

}
