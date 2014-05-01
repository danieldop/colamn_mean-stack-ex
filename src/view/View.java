package view;

import controller.Presenter;

public interface View extends Runnable
{
	/**
	 * 
	 * @param data
	 * refreshes the board data.
	 * if the data is held by a canvas, it should call redraw method.
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
}
