package model;

import controller.Presenter;

public interface Model 
{
	/**
	 * 
	 * @param userCommand
	 * @return true when a successful move was performed.
	 *		false otherwise. 
	 */
	public boolean doMovement(int userCommand);
	/**
	 * 
	 * @param userCommand
	 * @param dataHelper
	 * @return	true when a successful action was performed.
	 *		false otherwise.
	 */
	public boolean doTecAction(int userCommand,Object dataHelper);
	/**
	 * 
	 * @return board represented by int[][] object.
	 */
	public int[][] getData();
	public void addObserver(Presenter presenter);
	public int getScore();
}
