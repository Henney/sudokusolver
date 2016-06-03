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
	protected void showGrid(Grid g) {
		Platform.runLater(new Runnable() {			
            @Override
            public void run() {
            	view.setAndDisplayGrid(new UserGrid(g));
            }
        });
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			cancel();
		}
	}

}
