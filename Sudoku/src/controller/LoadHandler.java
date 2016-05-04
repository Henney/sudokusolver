package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.Grid;
import view.View;

public class LoadHandler<T> implements EventHandler<MouseEvent> {

	View view;
	
	public LoadHandler(View view) {
		this.view = view;
	}

	@Override
	public void handle(MouseEvent event) {
		FileChooser fc = new FileChooser();
		File file = fc.showOpenDialog(view.getStage());
		try {
			Grid grid = new Grid(new FileReader(file));
			view.setAndDisplayGrid(grid);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	}

}
