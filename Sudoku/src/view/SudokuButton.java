package view;

import javafx.scene.control.Button;

public class SudokuButton extends Button {
	
	private int index;

	public SudokuButton(int index) {
		super();
		this.index = index;
	}
	
	public SudokuButton(String text, int index) {
		super(text);
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
}
