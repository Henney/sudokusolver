package view;

import javafx.application.Platform;
import model.Grid;
import model.TacticSolver;
import model.UserGrid;

public class GuiTacticSolver extends TacticSolver {

	private View view;
	
	public GuiTacticSolver(Grid grid, View view) {
		super(grid);
		this.view = view;
	}

	@Override
	protected void showField(int field, int val) {
		if (view.getSolveSpeed() == 0) {
			return;
		}
		
		Platform.runLater(new Runnable() {			
            @Override
            public void run() {
            	view.setField(field, val);
            }
        });
		try {
			Thread.sleep(view.getSolveSpeed());
		} catch (InterruptedException e) {
			cancel();
		}
	}

	@Override
	protected void hideField(int field) {
		showField(field, 0);
	}

}
