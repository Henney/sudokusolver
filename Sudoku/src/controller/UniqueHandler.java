package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.TacticSolver;
import view.View;

public class UniqueHandler<T> extends ButtonHandler<MouseEvent> {

	public UniqueHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent event) {
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {					
				Platform.runLater(new Runnable() {
		            @Override public void run() {
						Alert alert = view.createMessageDialog("Unique? running",
								"We are calculating the uniqueness of the current puzzle.\nPlease wait!",
								AlertType.INFORMATION);

						TacticSolver s = new TacticSolver(view.getGrid());
						boolean solvable = s.unique();
						
						String msg = solvable ?
								"The current grid has a unique solution!" :
									"The current grid has more than one solution.";
						alert.close();								
						view.createMessageDialog("Does the current grid have a unique solution?", msg, AlertType.INFORMATION);
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
