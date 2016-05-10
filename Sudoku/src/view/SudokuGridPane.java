package view;

import javafx.scene.layout.GridPane;

public class SudokuGridPane extends GridPane {

	private int n;
	private int k;
	
	public SudokuGridPane(int k) {
		super();
		this.k = k;
		this.n = k*k;
	}
	
	public SudokuButton getField(int i) {
		int rowNo = i/n;
		int colNo = i%n;
		int noLinesRow = k-1;
		int offset = rowNo*noLinesRow + colNo/k + rowNo/k*(n+noLinesRow);
		i += offset;
		return (SudokuButton)getChildren().get(i);
	}
}
