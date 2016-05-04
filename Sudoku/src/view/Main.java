package view;
	
import java.io.IOException;

import controller.LoadHandler;
import controller.SudokuFieldController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Grid;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;


public class Main extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    private GridPane gridLayout;
    
    private Grid grid;
    
    private Button selectedField;
    
    private final int SIZE = 3; // TODO move to model
    
    private final int MIN_SIZE = 600;
    
    // css strings
    private final String BUTTON_CLASS = "fieldButton";
    private String noEffect = "-fx-effect: none;";
    private String selectedEffect = "-fx-effect: innershadow( three-pass-box , rgba(255.0,0,0,.5) , 2, 1, 0, 0);";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Sudoku");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("Smiley_icon.png")));
			this.primaryStage.setMinWidth(MIN_SIZE);
			this.primaryStage.setMinHeight(MIN_SIZE);
			initRootLayout();
			this.primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void initRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Markup.fxml"));
        rootLayout = (AnchorPane) loader.load();
        
        gridLayout = (GridPane) rootLayout.lookup("#SudokuBoardGrid");
        setupBasicSudoku();
        setSizeChangedListeners();

        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Button loadButton = (Button) rootLayout.lookup("#LoadButton");
		loadButton.setOnMouseClicked(new LoadHandler<MouseEvent>(this));
		Button saveButton = (Button) rootLayout.lookup("#SaveButton");
		Button fetchButton = (Button) rootLayout.lookup("#FetchButton");
		
        primaryStage.setScene(scene);
	}

	private void setSizeChangedListeners() {
		rootLayout.widthProperty().addListener((observable, oldValue, newValue) -> {
			int width = (int) ((Double)newValue).doubleValue();
			if (width > rootLayout.getHeight()) return;
			scaleSudoku(width);
		});

		rootLayout.heightProperty().addListener((observable, oldValue, newValue) -> {
			int height = (int) ((Double)newValue).doubleValue();
			if (height > rootLayout.getWidth()) return;
			scaleSudoku(height);
		});
	}

	private void scaleSudoku(int size) {
		int buttonWidth = (size/(SIZE*SIZE+1))*3/4;

		GridPane sudoku = (GridPane)gridLayout.getChildren().get(0);
		for (Node cell : sudoku.getChildren()) {
			if (cell instanceof Button) {
				((Button)cell).setMinSize(buttonWidth, buttonWidth);
				((Button)cell).setMaxSize(buttonWidth, buttonWidth);
			} else if (cell instanceof Line) {
				Line line = ((Line)cell);
				if (line.getEndX() > 1) {
					line.setEndX(buttonWidth);
				} else if (line.getEndY() > 1){
					line.setEndY(buttonWidth);
				}
			}
		}

		GridPane numbers = (GridPane)gridLayout.getChildren().get(1);
		for (Node cell : numbers.getChildren()) {
			if (cell instanceof Button) {
				((Button)cell).setMinSize(buttonWidth, buttonWidth);
				((Button)cell).setMaxSize(buttonWidth, buttonWidth);
			}
		}
	}

	private void setupBasicSudoku() {
		int pix = (int)gridLayout.getPrefHeight()/SIZE/SIZE;
		GridPane sudoku = new GridPane();
		GridPane numbers = new GridPane();
		int rowLines = 0;
		for (int i = 0; i < SIZE*SIZE+(SIZE-1); i++) {
			int colLines = 0;
			
			if (rowLines == SIZE) {
				sudoku.addRow(i);
				
				for (int j = 0; j < SIZE*SIZE+(SIZE-1); j++) {
					Line l = new Line(0,0,pix,0);
					
					if (colLines == SIZE) {
						l = new Line(0,0,1,0);
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
			
			for (int j = 0; j < SIZE*SIZE+(SIZE-1); j++) {
				if (colLines == SIZE) {
					sudoku.addColumn(j, new Line(0,0,0,pix));
					colLines = 0;
					j++;
				}
				
				Button b = new Button();
				b.setMinSize(pix, pix);
				b.setMaxSize(pix, pix);
				b.setAlignment(Pos.CENTER);
				b.getStyleClass().add(BUTTON_CLASS);
				b.setOnMouseClicked(new SudokuFieldController<MouseEvent>(this, b));
				sudoku.addColumn(j, b);
				colLines++;
			}
			rowLines++;
		}
		gridLayout.add(sudoku, 0, 0);
		
		numbers.setAlignment(Pos.TOP_RIGHT);
		for (int i = 1; i <= SIZE*SIZE; i++) {
			numbers.addRow(i-1);
			Button b = new Button(i+"");
			b.setMinSize(pix, pix);
			b.setMaxSize(pix, pix);
			b.setAlignment(Pos.CENTER);
			b.getStyleClass().add(BUTTON_CLASS);
			b.setOnMouseClicked(new numberFieldController<MouseEvent>(this, i));
			numbers.addColumn(0, b);
		}
		
		gridLayout.add(numbers, 1, 0);
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void setSelectedField(Button newField) {
		if (this.selectedField != null && this.selectedField != newField) {
			if (!this.selectedField.styleProperty().get().contains(noEffect)) {
				this.selectedField.setStyle(noEffect);
			}
		}
		
		if (newField.styleProperty().get().contains(selectedEffect)) {
			newField.setStyle(noEffect);
		} else {
			newField.setStyle(selectedEffect);
		}
		
		this.selectedField = newField;
	}

	public void inputNumber(int number) {
		this.selectedField.textProperty().set(number+"");
	}

	public Window getStage() {
		return this.primaryStage;
	}

	public void displayGrid(Grid grid) {
	}
}
