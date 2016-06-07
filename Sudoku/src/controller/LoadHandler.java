package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.UserGrid;
import view.View;

public class LoadHandler<T> extends ButtonHandler<MouseEvent> {

	public LoadHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent event) {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("puzzles")); // TODO Don't set? Just easier right now.
		//fc.getExtensionFilters().add(new ExtensionFilter("Text files", "*.txt"));
		File file = fc.showOpenDialog(view.getStage());
		try {
			UserGrid grid = new UserGrid(new FileReader(file));
			view.setAndDisplayGrid(grid);
		} catch (FileNotFoundException e) {
			view.createMessageDialogue("Error!", "The file doesn't exist!", AlertType.ERROR);
		} catch (IOException e) {
			view.createMessageDialogue("Error!", e.getMessage(), AlertType.ERROR);
		} catch (NullPointerException e) {
			//view.createMessageDialogue("No file", "No file selected", AlertType.INFORMATION); TODO: Just do nothing?
		} catch (Exception e) {
			view.createMessageDialogue("Error!",
					"Something is wrong with the loaded file. Please check your formatting",
					AlertType.ERROR);
		}
	}

}
