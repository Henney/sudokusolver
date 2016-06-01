package view;

import javafx.application.Platform;
import model.Grid;
import model.Solver;
import model.UserGrid;

public class GuiSolver extends Solver {

	private View view;
	
	public GuiSolver(Grid grid, View view) {
		super(grid);
		this.view = view;
	}

	@Override
	protected void showGrid(Grid g) {
		Platform.runLater(new Runnable() {
            @Override public void run() {
            	view.setAndDisplayGrid(new UserGrid(g));
            }
        });
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
