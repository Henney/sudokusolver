package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.TacticSolver;
import view.View;

public class SolvableHandler<T> extends ButtonHandler<MouseEvent> {

	public SolvableHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent event) {
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {					
				Platform.runLater(new Runnable() {
		            @Override public void run() {
						Alert alert = view.createMessageDialog("Solvable? running",
								"We are calculating the solvability of the current puzzle.\nPlease wait!",
								AlertType.INFORMATION);

						TacticSolver s = new TacticSolver(view.getGrid());
						boolean solvable = s.solvable();
						
						String msg = solvable ?
								"The current grid can be solved!" :
									"Unfortunately, this grid cannot be solved.";
						alert.close();								
						view.createMessageDialog("Is the current grid solvable?", msg, AlertType.INFORMATION);
		            }
		        });
				return true;
			}
		};
		
		Thread t = new Thread(task);	
		t.setDaemon(true); // Shut thread down on exit
		t.start();
	}

}
