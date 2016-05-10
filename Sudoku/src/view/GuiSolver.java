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
	protected Grid solve_helper(Grid g) {
		final Grid grid = new Grid(g);
		Platform.runLater(new Runnable() {
            @Override public void run() {
            	view.setAndDisplayGrid(new UserGrid(grid));
            }
        });
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g = super.solve_helper(g);
		return g;
	}

}
