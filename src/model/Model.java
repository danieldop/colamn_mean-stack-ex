package model;

import controller.Presenter;

public interface Model 
{
	public boolean doAction(int userCommand);
	public int[][] getData();
	public void addObserver(Presenter presenter);
}
