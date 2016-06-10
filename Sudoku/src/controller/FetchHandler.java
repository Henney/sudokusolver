package controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import com.jaunt.NotFound;
import com.jaunt.ResponseException;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.UserGrid;
import model.WebScraper;
import view.View;

public class FetchHandler<T> extends ButtonHandler<MouseEvent> {
	
	public FetchHandler(View view) {
		super(view);
	}

	@Override
	public void handle(MouseEvent arg0) {
		Optional<String> result = view.createFetchDialog();
		result.ifPresent(dif -> {
			int level = 0;
			switch(dif) {
			case "Easy": level = 1; break;
			case "Medium": level = 2; break;
			case "Hard": level = 3; break;
			case "Evil": level = 4; break;
			default: return;
			}
			
			UserGrid grid;
			try {
				grid = new UserGrid(new StringReader(WebScraper.getSudokuFromWeb(level)));
				view.setAndDisplayGrid(grid);
			} catch (NotFound e) {
				view.createMessageDialog("Error!",
						"The website did not load properly, so no sudoku was fetched.",
						AlertType.ERROR);
			} catch (ResponseException e) {
				view.createMessageDialog("Error!",
						"Connection to the server timed out.",
						AlertType.ERROR);
			} catch (IOException e) {
				view.createMessageDialog("Error!",
						"Something went wrong...",
						AlertType.ERROR);
				e.printStackTrace();
			}
		});
	}

}
