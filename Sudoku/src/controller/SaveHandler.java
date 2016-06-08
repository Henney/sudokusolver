package controller;

import java.io.File;

import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import model.Writer;
import view.View;

public class SaveHandler<T> extends ButtonHandler<MouseEvent> {

	public SaveHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent event) {
		DirectoryChooser dc = new DirectoryChooser();
		File f = dc.showDialog(view.getStage());
		try {
			Writer.writeToFile(view.getGrid(), f.getAbsolutePath()+"/");
		} catch (NullPointerException e) {
			// Do nothing
		}
	}

}
