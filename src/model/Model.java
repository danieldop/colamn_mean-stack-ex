package model;

import controller.Presenter;

public interface Model 
{
	public boolean doMovement(int userCommand);
	public boolean doTecAction(int userCommand,Object dataHelper);
	public int[][] getData();
	public void addObserver(Presenter presenter);
	public int getScore();
}
