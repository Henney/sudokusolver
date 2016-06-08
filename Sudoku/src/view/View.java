package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import controller.CancelHandler;
import controller.FetchHandler;
import controller.FinishHandler;
import controller.GenerateHandler;
import controller.InputHandler;
import controller.LoadHandler;
import controller.SolveHandler;
import controller.WindowResizeListener;
import controller.NumberFieldController;
import controller.SATHandler;
import controller.SaveHandler;
import controller.SolvableHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Grid;
import model.SATSolver;
import model.Solver;
import model.TacticSolver;
import model.UserGrid;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class View extends Application {
	
	private Solver cSolver;
	private Service<UserGrid> service;
	private Task<UserGrid> solveTask;
	private int solveSpeed = SpeedListener.MAX_DELAY;
	
	public enum Method { Tactic, SAT };

	private Stage primaryStage;
	private AnchorPane rootLayout;
	private HBox gameLayout;
	
	private SudokuGridPane sudokuGrid;

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

		gameLayout = (HBox) rootLayout.lookup("#SudokuBoardPane");
		gameLayout.toBack();
		setupSudoku(DEFAULT_K);
		setSizeChangedListeners(DEFAULT_K);

		addButtonListeners(rootLayout);
		
		Slider speedSlider = (Slider) rootLayout.lookup("#SpeedSlider");
		speedSlider.valueProperty().addListener(new SpeedListener<Number>(this));
		// Set min and max amount of numbers input per second when solving
		speedSlider.setMin(1.0);
		speedSlider.setMax(100.0);

		// Show the scene containing the root layout.
		Scene scene = new Scene(rootLayout);
		scene.getStylesheets().add(getClass().getResource("stylesheets/application.css").toExternalForm());
		primaryStage.setScene(scene);
	}

	private void addButtonListeners(AnchorPane rootLayout2) {
		// Find buttons and set their listeners
		Button generateButton = (Button) rootLayout.lookup("#GenerateButton");
		generateButton.setOnMouseClicked(new GenerateHandler<MouseEvent>(this));
		Button fetchButton = (Button) rootLayout.lookup("#FetchButton");
		fetchButton.setOnMouseClicked(new FetchHandler<MouseEvent>(this));
		Button loadButton = (Button) rootLayout.lookup("#LoadButton");
		loadButton.setOnMouseClicked(new LoadHandler<MouseEvent>(this));
		
		Button solveButton = (Button) rootLayout.lookup("#SolveButton");
		solveButton.setOnMouseClicked(new SolveHandler<MouseEvent>(this));
		Button SATButton = (Button) rootLayout.lookup("#SATButton");
		SATButton.setOnMouseClicked(new SATHandler<MouseEvent>(this));
		Button finishButton = (Button) rootLayout.lookup("#FinishButton");
		finishButton.setOnMouseClicked(new FinishHandler<MouseEvent>(this));

		Button solvableButton = (Button) rootLayout.lookup("#SolvableButton");
		solvableButton.setOnMouseClicked(new SolvableHandler<MouseEvent>(this));
		Button cancelButton = (Button) rootLayout.lookup("#CancelButton");
		cancelButton.setOnMouseClicked(new CancelHandler<MouseEvent>(this));
		Button saveButton = (Button) rootLayout.lookup("#SaveButton");
		saveButton.setOnMouseClicked(new SaveHandler<MouseEvent>(this));
	}

	private void setSizeChangedListeners(int k) {
		BorderPane bp = (BorderPane) rootLayout.getChildren().get(0);
		try {
			bp.widthProperty().removeListener(this.widthListener);
			bp.heightProperty().removeListener(this.heightListener);
		} catch (NullPointerException e) {
			// Do nothing
		}

		WindowResizeListener widthListener = new WindowResizeListener(this, k, WindowResizeListener.Property.Width);
		bp.widthProperty().addListener(widthListener);
		this.widthListener = widthListener;

		WindowResizeListener heightListener = new WindowResizeListener(this, k, WindowResizeListener.Property.Height);
		bp.heightProperty().addListener(heightListener);
		this.heightListener = heightListener;
	}

	public void scaleSudoku(int size, int k) {
		int buttonWidth = size / (k*k+1);
		Font font = new Font(buttonWidth/3);
		
		sudokuGrid.scale(buttonWidth, font);

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
		((HBox) gameLayout).getChildren().clear();

		int pix = 30;
		sudokuGrid = new SudokuGridPane(grid);
		GridPane numbers = new GridPane();
		sudokuGrid.setup();
		gameLayout.getChildren().add(sudokuGrid);
		AnchorPane.setLeftAnchor(sudokuGrid, 0.0);
		sudokuGrid.addEventHandler(KeyEvent.KEY_PRESSED, new InputHandler(sudokuGrid));

		for (int i = 1; i <= k * k; i++) {
			numbers.addRow(i - 1);
			Button b = new Button(i + "");
			b.setMinSize(pix, pix);
			b.setMaxSize(pix, pix);
			b.setAlignment(Pos.CENTER);
			b.getStyleClass().add(BUTTON_CLASS);
			b.setOnMouseClicked(new NumberFieldController<MouseEvent>(sudokuGrid, i));
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
		sudokuGrid.displayGrid();
	}
	
	public void displayGrid(UserGrid grid) {
		this.grid = grid;
		sudokuGrid.setGrid(grid);
		sudokuGrid.displayGrid();
	}
	
	public Grid getGrid() {
		return grid;
	}

	public AnchorPane getRootLayout() {
		return this.rootLayout;
	}

	public void solveSudoku(Method m) {
		if (!cancelSolveTask()) {
			return;
		}
		
		service = new Service<UserGrid>() {

			@Override
			protected Task<UserGrid> createTask() {
				solveTask = new Task<UserGrid>() {
					
					@Override
					protected UserGrid call() throws Exception {
						Grid solvedGrid = null;
						switch (m) {
						case Tactic:
							cSolver = new GuiTacticSolver(new Grid(grid), View.this);
							solvedGrid = cSolver.solve();
							break;
						case SAT:
							cSolver = new SATSolver(new Grid(grid));
							solvedGrid = cSolver.solve();
							break;
						}
		            	
		            	if (solvedGrid != null) {
		            		UserGrid ug = new UserGrid(solvedGrid);
							Platform.runLater(new Runnable() {
					            @Override public void run() {
									displayGrid(ug);
					            	enableSlider();
					            }
					        });		            		
		            	} else {
							Platform.runLater(new Runnable() {
					            @Override public void run() {
				            		createMessageDialogue("Error!",
				            				"The given grid configuration is unsolvable.",
				            				AlertType.ERROR);
									displayGrid(new UserGrid(cSolver.getGrid()));
									enableSlider();
					            }
					        });	
		            	}
						
						return null;
					}
				};
				return solveTask;
			}
		};
		
		service.start();
	}
	
	public void enableSlider() {
		Slider sld = (Slider) primaryStage.getScene().lookup("#SpeedSlider");
		sld.disableProperty().set(false);
		sld.setValue(sld.getValue());		
	}
	
	public boolean cancelSolveTask() {
		if (solveTask == null || solveTask.isDone()) {
			return true;
		}
		enableSlider();
		cSolver.cancel();
		boolean b = service.cancel();
		if (!b) {
			// TODO throw an exception in a window?
		}
		return true;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public int getSolveSpeed() {
		return solveSpeed;
	}

	public void setSolveSpeed(int s) {
		solveSpeed = s;
	}
	
	public Alert createMessageDialogue(String title, String msg, AlertType type) {
		Alert alert = new Alert(type);
		alert.initOwner(primaryStage);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
		return alert;
	}
	
	public Optional<String> createGenerateDialog() {
		new TextInputDialog();
		TextInputDialog dialog = new TextInputDialog(String.valueOf(DEFAULT_K));
		dialog.initOwner(primaryStage);
		dialog.setTitle("Generate sudoku");
		dialog.setHeaderText(null);
		dialog.setContentText("Please enter size k (2 <= k <= 6):");
		return dialog.showAndWait();
	}
	
	public Optional<String> createFetchDialog() {
		List<String> choices = new ArrayList<>();
		choices.add("Easy");
		choices.add("Medium");
		choices.add("Hard");
		choices.add("Evil");

		ChoiceDialog<String> dialog = new ChoiceDialog<>("Medium", choices);
		dialog.setTitle("Difficulty");
		dialog.setHeaderText(null);
		dialog.setContentText("Select a difficulty:");

		return dialog.showAndWait();
	}

	public HBox getGameLayout() {
		return gameLayout;
	}
	
	public void resetSize() {
		scaleSudoku(newSize(), grid.k());
	}
	
	public int newSize() {
		BorderPane bp = (BorderPane) rootLayout.getChildren().get(0);
		int offset = (int) ((Pane)bp.getBottom()).getHeight() + 5*grid.k();
		
		double h = bp.getHeight()-offset;
		double w = bp.getWidth();
		double newValue = h < w ? h : w;
		return (int)newValue;
	}

	public void setField(int field, int val) {
		sudokuGrid.setFieldText(sudokuGrid.getField(field), String.valueOf(val));
	}
}
