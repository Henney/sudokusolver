package controller;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import com.jaunt.NotFound;
import com.jaunt.ResponseException;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import model.UserGrid;
import model.WebScraper;
import view.View;

public class FetchHandler<T> implements EventHandler<MouseEvent> {

	View view;
	
	public FetchHandler(View view) {
		this.view = view;
	}

	@Override
	public void handle(MouseEvent arg0) {
		int level = 4; // TODO
		UserGrid grid;
		try {
			grid = new UserGrid(new StringReader(new WebScraper().getSudokuFromWeb(level)));
			view.setAndDisplayGrid(grid);
		} catch (NotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
