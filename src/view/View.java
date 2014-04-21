package view;

import controller.Presenter;

public interface View extends Runnable
{
	public void displayData(final int[][] data);
	public int getUserCommand();
	public void addObserver(Presenter presenter);
	public void displayScore(final String score);
}
