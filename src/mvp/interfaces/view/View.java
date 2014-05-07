package mvp.interfaces.view;

import mvp.controller.Presenter;

public interface View extends Runnable
{
	/**

	 * @param int[][] data =>board to display on the canvas.
	 * calls the redraw of the canvas(on a synch Execution call) 
	 *
	 */
	public void displayData(final int[][] data);
	public int getUserCommand();
	public void addObserver(Presenter presenter);
	/**
	 * 
	 * @param score
	 * refreshes the score data.
	 * if the data is held by a canvas, it should call redraw method.
	 */
	public void displayScore(final String score);
	
	public void showWonGame();
	public void showLostGame();
}
