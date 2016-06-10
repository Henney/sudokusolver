package controller;

import java.util.Optional;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.Grid;
import model.PuzzleGenerator;
import model.UserGrid;
import view.View;

public class GenerateHandler<T> extends ButtonHandler<MouseEvent> {
	
	public GenerateHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent event) {
		Optional<String> result = view.createGenerateDialog();
		try {
			result.ifPresent(size -> {
				int k = Integer.parseInt(size);
				if (2 <= k && k <= 6) {
					createGeneratorThread(k);
				} else {
					throw new IllegalArgumentException();
				}
			});
		} catch (IllegalArgumentException e) {
			view.createMessageDialogue("Syntax error", "k must be an integer between 2 and 6", AlertType.ERROR);
		}
	}
	
	public void createGeneratorThread(int k) {
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {					
				Platform.runLater(new Runnable() {
		            @Override public void run() {
						Alert alert = view.createMessageDialogue("Generating puzzle",
								"We are generating your puzzle.\nThis may take a while",
								AlertType.INFORMATION);
						
						Grid g = PuzzleGenerator.generate(k);
						
						alert.close();
						
						view.setAndDisplayGrid(new UserGrid(g));
						
						view.createMessageDialogue("Puzzle generated",
								"Your puzzle has been created and will be loaded to the grid.",
								AlertType.INFORMATION);
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
