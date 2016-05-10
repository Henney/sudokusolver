package view;

import java.io.IOException;

import controller.InputHandler;
import controller.LoadHandler;
import controller.SolveHandler;
import controller.SudokuFieldController;
import controller.WindowResizeListener;
import controller.numberFieldController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Grid;
import model.Solver;
import model.UserGrid;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class View extends Application {

	private Stage primaryStage;
	private AnchorPane rootLayout;
	private AnchorPane gameLayout;
	
	private SudokuGridPane sudokuPane;

	UserGrid grid;

	private final int DEFAULT_K = 3;

	private final int MIN_WINDOW_SIZE = 600;
	private WindowResizeListener widthListener;
	private WindowResizeListener heightListener;

	// css strings
	static final String BUTTON_CLASS = "fieldButton";

	@Override
	public void start(Stage primaryStage) {
		try {
			this.grid = new UserGrid(DEFAULT_K);
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Sudoku");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("Smiley_icon.png")));
			this.primaryStage.setMinWidth(MIN_WINDOW_SIZE);
			this.primaryStage.setMinHeight(MIN_WINDOW_SIZE);
			initLayout();
			this.primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void initLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(View.class.getResource("Markup.fxml"));
		rootLayout = (AnchorPane) loader.load();

		gameLayout = (AnchorPane) rootLayout.lookup("#SudokuBoardPane");
		setupSudoku(DEFAULT_K);
		scaleSudoku(MIN_WINDOW_SIZE, DEFAULT_K);
		setSizeChangedListeners(DEFAULT_K);

		// Find buttons and set their listeners
		Button loadButton = (Button) rootLayout.lookup("#LoadButton");
		loadButton.setOnMouseClicked(new LoadHandler<MouseEvent>(this));
		Button saveButton = (Button) rootLayout.lookup("#SaveButton");
		Button fetchButton = (Button) rootLayout.lookup("#FetchButton");
		Button solveButton = (Button) rootLayout.lookup("#SolveButton");
		solveButton.setOnMouseClicked(new SolveHandler<MouseEvent>(this));

		// Show the scene containing the root layout.
		Scene scene = new Scene(rootLayout);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new InputHandler(sudokuPane));
		primaryStage.setScene(scene);
	}

	private void setSizeChangedListeners(int k) {
		try {
			rootLayout.widthProperty().removeListener(this.widthListener);
			rootLayout.heightProperty().removeListener(this.heightListener);
		} catch (NullPointerException e) {
			// Do nothing
		}

		WindowResizeListener widthListener = new WindowResizeListener(this, k, WindowResizeListener.Property.Width);
		rootLayout.widthProperty().addListener(widthListener);
		this.widthListener = widthListener;

		WindowResizeListener heightListener = new WindowResizeListener(this, k, WindowResizeListener.Property.Height);
		rootLayout.heightProperty().addListener(heightListener);
		this.heightListener = heightListener;
	}

	public void scaleSudoku(int size, int k) {
		int buttonWidth = (size / (k * k + 1)) * 3 / 4;
		Font font = new Font(buttonWidth/4);
		
		sudokuPane.scale(buttonWidth, font);

		GridPane numbers = (GridPane) gameLayout.getChildren().get(1);
		for (Node cell : numbers.getChildren()) {
			if (cell instanceof Button) {
				((Button) cell).setMinSize(buttonWidth, buttonWidth);
				((Button) cell).setMaxSize(buttonWidth, buttonWidth);
				((Button) cell).setFont(font);
			}
		}
		numbers.setMaxWidth(buttonWidth);
	}

	private void setupSudoku(int k) {
		((AnchorPane) gameLayout).getChildren().clear();

		int pix = 30;// (int)gridLayout.getPrefHeight()/k/k;
		sudokuPane = new SudokuGridPane(grid);
		GridPane numbers = new GridPane();
		sudokuPane.setup();
		gameLayout.getChildren().add(sudokuPane);
		AnchorPane.setLeftAnchor(sudokuPane, 0.0);

		for (int i = 1; i <= k * k; i++) {
			numbers.addRow(i - 1);
			Button b = new Button(i + "");
			b.setMinSize(pix, pix);
			b.setMaxSize(pix, pix);
			b.setAlignment(Pos.CENTER);
			b.getStyleClass().add(BUTTON_CLASS);
			b.setOnMouseClicked(new numberFieldController<MouseEvent>(sudokuPane, i));
			numbers.addColumn(0, b);
		}
		gameLayout.getChildren().add(numbers);
		AnchorPane.setRightAnchor(numbers, 0.0);
	}

	public Window getStage() {
		return this.primaryStage;
	}

	public void setAndDisplayGrid(UserGrid grid) {
		this.grid = grid;
		setupSudoku(grid.k());
		scaleSudoku((int)rootLayout.getHeight(), grid.k());
		setSizeChangedListeners(grid.k());
		sudokuPane.displayGrid();
	}

	public AnchorPane getRootLayout() {
		return this.rootLayout;
	}

	public void solveSudoku() {
		
		Task<UserGrid> task = new Task<UserGrid>() {
			
			@Override
			protected UserGrid call() throws Exception {
				Solver s = new Solver(grid.getGrid());
				Grid solvedGrid = s.solve();
				Platform.runLater(new Runnable() {
                    @Override public void run() {
                    	grid = new UserGrid(solvedGrid);
                		sudokuPane.displayGrid();
                    }
                });
				return grid;
			}
			
		};
		
		Thread t = new Thread(task);
		t.setDaemon(true); // thread will not prevent application shutdown
		t.start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
