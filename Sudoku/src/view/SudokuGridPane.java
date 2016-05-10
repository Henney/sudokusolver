package view;

import java.util.Set;

import controller.SudokuFieldController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import model.Grid;
import model.UserGrid;
import model.util.Pair;

public class SudokuGridPane extends GridPane {

	private UserGrid grid;
	private int n;
	private int k;

	SudokuButton selectedField;
	
	// css strings
	static final String CONFLICT_CLASS = "conflict";
	static final String SELECTED_CLASS = "selected";
	
	public SudokuGridPane(UserGrid grid) {
		super();
		this.grid = grid;
		this.k = grid.k();
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

	public void scale(int buttonWidth, Font font) {
		for (Node cell : getChildren()) {
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
	}

	public void setup() {
		int pix = 30;
		int butIndex = 0;
		int rowLines = 0;
		for (int i = 0; i < k * k + (k - 1); i++) {
			int colLines = 0;

			if (rowLines == k) {
				addRow(i);

				for (int j = 0; j < k * k + (k - 1); j++) {
					Line l = new Line(0, 0, pix, 0);

					if (colLines == k) {
						l = new Line(0, 0, 1, 0);
						colLines = -1;
					}

					addColumn(j, l);
					colLines++;
				}
				rowLines = 0;
				i++;
			}

			addRow(i);
			colLines = 0;

			for (int j = 0; j < k * k + (k - 1); j++) {
				if (colLines == k) {
					addColumn(j, new Line(0, 0, 0, pix));
					colLines = 0;
					j++;
				}

				SudokuButton b = new SudokuButton(butIndex);
				b.setMinSize(pix, pix);
				b.setMaxSize(pix, pix);
				b.setAlignment(Pos.CENTER);
				b.getStyleClass().add(View.BUTTON_CLASS);
				b.setOnMouseClicked(new SudokuFieldController<MouseEvent>(this, b));
				addColumn(j, b);
				butIndex++;
				colLines++;
			}
			rowLines++;
		}
	}

	void displayGrid() {
		int i = 0;

		for (Node cell : getChildren()) {
			if (cell instanceof Button) {
				int number = grid.get(i);
				if (number != 0) {
					((Button) cell).setText(number + "");
				}
				i++;
			}
		}
	}

	public void setSelectedField(SudokuButton newField) {
		if (selectedField != null && selectedField != newField) {
			selectedField.getStyleClass().remove(SELECTED_CLASS);
			String s = selectedField.getText();
			if (!s.isEmpty()) {
				int x = Integer.parseInt(s);
				if (x > n || x <= 0) {
					setSelectedFieldText("");
				}
			}
		}
		if (newField.equals(selectedField)) {
			newField.getStyleClass().remove(SELECTED_CLASS);
			selectedField = null;
		} else {
			newField.getStyleClass().add(SELECTED_CLASS);
			selectedField = newField;
		}

	}

	public void moveSelectedField(int x, int y) {
		int newRow = Math.floorMod(selectedField.getIndex()/n+y, n);
		int newCol = Math.floorMod(selectedField.getIndex()+x, n);
		int newIndex = newRow*n+newCol;
		setSelectedField(getField(newIndex));
	}

	public Button getSelectedField() {
		return this.selectedField;
	}
	
	public void setSelectedFieldText(String s) {
		selectedField.setText(s);
		int index = selectedField.getIndex();
		Pair<Set<Integer>, Set<Integer>> conflicts;
		if (s.isEmpty()) {
			conflicts = grid.set(index, 0);
			setConflict(index, false);
		} else {
			int value = Integer.parseInt(s);
			if (value < n) {
				conflicts = grid.set(index, value);
			} else {
				return;
			}
		}
		
		for (int i : conflicts.fst) {
			setConflict(i, true);
		}
		for (int i : conflicts.snd) {
			setConflict(i, false);
		}
	}
	
	private void setConflict(int index, boolean conflict) {
		getField(index).getStyleClass().remove(CONFLICT_CLASS);
		if (conflict) getField(index).getStyleClass().add(CONFLICT_CLASS);
	}

	public void inputNumber(int number) {
		if (selectedField != null) {
			selectedField.setText("");
			inputNumberNoDelete(number);
		}
	}

	public void inputNumberNoDelete(int i) {
		setSelectedFieldText(selectedField.getText() + String.valueOf(i));
	}

	public void clearSelectedField() {
		setSelectedFieldText("");
		grid.set(selectedField.getIndex(), 0);
	}
}
