package model;

import java.util.Observable;

import consts.CommandsConsts;
import controller.Presenter;

public class Model2048 extends Observable implements Model {

	private int[][] game;

	public Model2048() {

	}

	@Override
	public int[][] getData() {

		return game;
	}

	private boolean moveRight() {
		try {
			int x = game.length - 1;
			
			for (int i = 0; i < game.length; i++) {
				for (int j = x; j > 0; j--) {
					if (game[i][j] >1 && game[i][j] == game[i][j - 1]) {
						game[i][j] = 2 * game[i][j];
						game[i][j - 1] = -1;
					} else if (game[i][j] == 0 && game[i][j - 1] != 0) {
						game[i][j] = game[i][j - 1];
						game[i][j - 1] = 0;
						j +=2;
					}
				}
				for (int j = 0; j + 1 < game.length; j++) {
					if (game[i][j] < 2) {
						game[i][j] = 0;
						if (j > 0) {
							game[i][j] = game[i][j - 1];
							game[i][j - 1] = 0;
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	private boolean moveLeft() {
		try {
			
			for (int i = 0; i < game.length; i++) {
				for (int j = 0; j <game.length-1; j++) {
					if (game[i][j] >1 && game[i][j] == game[i][j + 1]) {
						game[i][j] = 2 * game[i][j];
						game[i][j + 1] = -1;
					} else if (game[i][j] == 0 && game[i][j + 1] != 0) {
						game[i][j] = game[i][j + 1];
						game[i][j + 1] = 0;
						j -=2;
					}
				}
				for (int j = 0; j + 1 < game.length; j++) {
					if (game[i][j] < 2) {
						game[i][j] = 0;
						if (j > 0) {
							game[i][j] = game[i][j + 1];
							game[i][j + 1] = 0;
						
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	private boolean move(int iTo, int jTo) {

		// for(int i=0;i<maze.length;i++)
		// {
		// for(int j=0;j<maze[i].length;j++)
		// {
		// if(maze[i][j] == 1)
		// {
		// if(i+iTo>-1 && i+iTo<maze.length && j+jTo>-1 &&
		// j+jTo<maze[i+iTo].length)//range check..
		// {
		// if(maze[i+iTo][j+jTo]!=-1)
		// {
		// if(maze[i+iTo][j+jTo] == 2)
		// maze[i+iTo][j+jTo] = 3;//winning mouse
		// else
		// maze[i+iTo][j+jTo] = 1;
		// maze[i][j] = 0;
		// return true;
		// }
		// }
		// return false;
		// }
		// }
		// }
		return false;
	}

	@Override
	public void addObserver(Presenter presenter) {
		super.addObserver(presenter);
	}

	@Override
	public boolean doAction(int userCommand) {
		switch (userCommand) {
		case CommandsConsts.UP:
			// return moveUp();// UP
		case CommandsConsts.DOWN:
			//return moveDown();// DOWN
		case CommandsConsts.LEFT:
			return moveLeft();// LEFT
		case CommandsConsts.RIGHT:
			return moveRight();// RIGHT
		case CommandsConsts.UP_LEFT:
			return move(-1, -1);// DIAGONAL_UP_LEFT
		case CommandsConsts.UP_RIGHT:
			return move(-1, 1);// DIAGONAL_UP_RIGHT
		case CommandsConsts.DOWN_LEFT:
			return move(1, -1);// DIAGONAL_DOWN_LEFT
		case CommandsConsts.DOWN_RIGHT:
			return move(1, 1);// DIAGONAL_DOWN_RIGHT
		case CommandsConsts.INITIATE: {
			this.initiateMaze();
			return true;
		}
		}
		return false;// NOT LEGAL ACTION.
	}

	private void initiateMaze() {
		this.game = new int[][] { { 2, 2, 0, 4 }, { 4, 0, 0, 4 },
				{8, 8, 8, 8 }, { 4, 4, 4, 2 } };
	}

}
