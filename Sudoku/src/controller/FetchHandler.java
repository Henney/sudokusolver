package controller;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import com.jaunt.NotFound;
import com.jaunt.ResponseException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.UserGrid;
import model.WebScraper;
import view.View;

public class FetchHandler<T> implements EventHandler<MouseEvent> {

	private View view;
	private int level = -1;
	
	public FetchHandler(View view) {
		this.view = view;
	}

	@Override
	public void handle(MouseEvent arg0) {
		showOptions();
		if (level < 1) return;
		UserGrid grid;
		try {
			grid = new UserGrid(new StringReader(WebScraper.getSudokuFromWeb(level)));
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
		level = -1;
	}
	
	public void showOptions() {
		Stage stage; 
		AnchorPane root = null;
		stage = new Stage();
		try {
			root = FXMLLoader.load(getClass().getResource("../view/DifficultyWindow.fxml"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.setTitle("Select a difficulty");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(view.getStage());
		stage.setResizable(false);
		setButtonClickHandlers(stage);
		
		stage.showAndWait();
	}

	private void setButtonClickHandlers(Stage stage) {
		Scene scene = stage.getScene();
		scene.lookup("#Easy").setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				level = 1;
				stage.close();
			}
		});
		scene.lookup("#Medium").setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				level = 2;
				stage.close();
			}
		});
		scene.lookup("#Hard").setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				level = 3;
				stage.close();
			}
		});
		scene.lookup("#Evil").setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				level = 4;
				stage.close();
			}
		});
		scene.lookup("#Cancel").setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				level = -1;
				stage.close();
			}
		});
	}

}
