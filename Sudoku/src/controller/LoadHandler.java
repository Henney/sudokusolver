package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.UserGrid;
import view.View;

public class LoadHandler<T> extends ButtonHandler<MouseEvent> {

	public LoadHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent event) {
		FileChooser fc = new FileChooser();
		File file = fc.showOpenDialog(view.getStage());
		try {
			UserGrid grid = new UserGrid(new FileReader(file));
			view.setAndDisplayGrid(grid);
		} catch (FileNotFoundException e) {
			view.createMessageDialog("Error!", "The file doesn't exist!", AlertType.ERROR);
		} catch (IOException e) {
			view.createMessageDialog("Error!", e.getMessage(), AlertType.ERROR);
		} catch (NullPointerException e) {
			// Do nothing when no file selected
		}  catch (IllegalArgumentException e) {
			view.createMessageDialog("Error!", e.getMessage(), AlertType.ERROR);
			System.out.println(e.getMessage());
		} catch (Exception e) {
			view.createMessageDialog("Error!",
					"Something is wrong with the loaded file. Please check your formatting",
					AlertType.ERROR);
		}
	}

}
