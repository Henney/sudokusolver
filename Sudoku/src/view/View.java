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

	private Grid grid;

	private SudokuButton selectedField;

	private final int DEFAULT_K = 3;

	private final int MIN_WINDOW_SIZE = 600;
	private WindowResizeListener widthListener;
	private WindowResizeListener heightListener;

	// css strings
	private final String BUTTON_CLASS = "fieldButton";
	private String noEffect = "-fx-effect: none;";
	private String selectedEffect = "-fx-effect: innershadow( three-pass-box , rgba(255.0,0,0,.5) , 2, 1, 0, 0);";

	@Override
	public void start(Stage primaryStage) {
		try {
			this.grid = new Grid(DEFAULT_K);
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
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new InputHandler(this));
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
		
		GridPane sudoku = (GridPane) gameLayout.getChildren().get(0);
		for (Node cell : sudoku.getChildren()) {
			if (cell instanceof Button) {
				((Button) cell).setMinSize(buttonWidth, buttonWidth);
				((Button) cell).setMaxSize(buttonWidth, buttonWidth);
				((Button) cell).setFont(font);
			} else if (cell instanceof Line) {
				Line line = ((Line) cell);
				if (line.getEndX() > 1) {
					line.setEndX(buttonWidth);
				} else if (line.getEndY() > 1) {
					line.setEndY(buttonWidth);
				}
			}
		}

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
		GridPane sudoku = new GridPane();
		GridPane numbers = new GridPane();
		int butIndex = 0;
		int rowLines = 0;
		for (int i = 0; i < k * k + (k - 1); i++) {
			int colLines = 0;

			if (rowLines == k) {
				sudoku.addRow(i);

				for (int j = 0; j < k * k + (k - 1); j++) {
					Line l = new Line(0, 0, pix, 0);

					if (colLines == k) {
						l = new Line(0, 0, 1, 0);
						colLines = -1;
					}

					sudoku.addColumn(j, l);
					colLines++;
				}
				rowLines = 0;
				i++;
			}

			sudoku.addRow(i);
			colLines = 0;

			for (int j = 0; j < k * k + (k - 1); j++) {
				if (colLines == k) {
					sudoku.addColumn(j, new Line(0, 0, 0, pix));
					colLines = 0;
					j++;
				}

				SudokuButton b = new SudokuButton(butIndex);
				b.setMinSize(pix, pix);
				b.setMaxSize(pix, pix);
				b.setAlignment(Pos.CENTER);
				b.getStyleClass().add(BUTTON_CLASS);
				b.setOnMouseClicked(new SudokuFieldController<MouseEvent>(this, b));
				sudoku.addColumn(j, b);
				butIndex++;
				colLines++;
			}
			rowLines++;
		}
		gameLayout.getChildren().add(sudoku);
		AnchorPane.setLeftAnchor(sudoku, 0.0);

		for (int i = 1; i <= k * k; i++) {
			numbers.addRow(i - 1);
			Button b = new Button(i + "");
			b.setMinSize(pix, pix);
			b.setMaxSize(pix, pix);
			b.setAlignment(Pos.CENTER);
			b.getStyleClass().add(BUTTON_CLASS);
			b.setOnMouseClicked(new numberFieldController<MouseEvent>(this, i));
			numbers.addColumn(0, b);
		}
		gameLayout.getChildren().add(numbers);
		AnchorPane.setRightAnchor(numbers, 0.0);
	}

	public void setSelectedField(SudokuButton newField) {
		if (selectedField != null && selectedField != newField
				&& !selectedField.styleProperty().get().contains(noEffect)) {
			selectedField.setStyle(noEffect);
			String s = selectedField.getText();
			if (!s.isEmpty()) {
				int n = Integer.parseInt(s);
				if (n > grid.size() || n <= 0) {
					selectedField.setText("");
				}
			}
		}

		if (newField.styleProperty().get().contains(selectedEffect)) {
			newField.setStyle(noEffect);
			selectedField = null;
		} else {
			newField.setStyle(selectedEffect);
			selectedField = newField;
		}

	}

	public Window getStage() {
		return this.primaryStage;
	}

	public void setAndDisplayGrid(Grid grid) {
		this.grid = grid;
		setupSudoku(grid.k());
		scaleSudoku((int)rootLayout.getHeight(), grid.k());
		setSizeChangedListeners(grid.k());
		displayGrid();
	}

	private void displayGrid() {
		int i = 0;
		GridPane sudokuGrid = (GridPane) gameLayout.getChildren().get(0);
		for (Node cell : sudokuGrid.getChildren()) {
			if (cell instanceof Button) {
				int number = grid.get(i);
				if (number != 0) {
					((Button) cell).setText(number + "");
				}
				i++;
			}
		}
	}

	public AnchorPane getRootLayout() {
		return this.rootLayout;
	}

	public void solveSudoku() {
		
		Task<Grid> task = new Task<Grid>() {
			
			@Override
			protected Grid call() throws Exception {
				Solver s = new Solver(grid);
				Grid solvedGrid = s.solve();
				Platform.runLater(new Runnable() {
                    @Override public void run() {
                    	grid = solvedGrid;
                		displayGrid();
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

	public Button getSelectedField() {
		return this.selectedField;
	}
	
	public void setSelectedFieldText(String s) {
		selectedField.setText(s);
		grid.set(selectedField.getIndex(), s.isEmpty() ? 0 : Integer.parseInt(s));
	}

	public void inputNumber(int number) {
		selectedField.setText("");
		inputNumberNoDelete(number);
	}

	public void inputNumberNoDelete(int i) {
		setSelectedFieldText(selectedField.getText() + String.valueOf(i));
	}

	public void clearSelectedField() {
		setSelectedFieldText("");
	}
}
