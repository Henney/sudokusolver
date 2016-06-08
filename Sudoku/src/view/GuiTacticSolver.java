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
		if (view.getSolveSpeed() == 0) {
			return;
		}
		
		Platform.runLater(new Runnable() {			
            @Override
            public void run() {
            	view.setAndDisplayGrid(new UserGrid(g));
            }
        });
		try {
			Thread.sleep(view.getSolveSpeed());
		} catch (InterruptedException e) {
			cancel();
		}
	}

}
