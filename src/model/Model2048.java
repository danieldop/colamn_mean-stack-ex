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

			for (int i = 0; i < game.length; i++) {
				for (int j = game.length - 1; j > 0; j--) {
					if (game[i][j] > 1 && game[i][j] == game[i][j - 1]) {
						game[i][j] = 2 * game[i][j];
						game[i][j - 1] = -1;
					} else if (game[i][j] == 0 && game[i][j - 1] != 0) {
						game[i][j] = game[i][j - 1];
						game[i][j - 1] = 0;
						if (j < game.length-1) {
							j += 2;
						}
					}
				}
				for (int j = game.length - 1; j >= 0; j--) {
					if (game[i][j] == -1) {
						game[i][j] = 0;
					}
					while (j > 0 && game[i][j] < 2 && game[i][j - 1] > 0) {
						game[i][j] = game[i][j - 1];
						game[i][j - 1] = 0;
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
			rotateMatrix();
			rotateMatrix();
			moveRight();
			rotateMatrix();
			rotateMatrix();
			// for (int i = 0; i < game.length; i++) {
			// for (int j = 0; j < game.length - 1; j++) {
			// if (game[i][j] > 1 && game[i][j] == game[i][j + 1]) {
			// game[i][j] = 2 * game[i][j];
			// game[i][j + 1] = -1;
			// } else if (game[i][j] == 0 && game[i][j + 1] != 0) {
			// game[i][j] = game[i][j + 1];
			// game[i][j + 1] = 0;
			// if (j > 0) {
			// j -= 2;
			// }
			//
			// }
			// }
			// for (int j = 1; j < game.length; j++) {
			// if (game[i][j] == -1) {
			// game[i][j] = 0;
			// }
			//
			// while (game[i][j - 1] < 2 && game[i][j] > 0 && j > 0) {
			// game[i][j - 1] = game[i][j];
			// game[i][j] = 0;
			// --j;
			// }
			//
			// }
			// }
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	private boolean moveUp() {

		try {
			rotateMatrix();
			moveRight();
			rotateMatrix();
			rotateMatrix();
			rotateMatrix();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	private boolean moveDown() {
		try {
			rotateMatrix();
						rotateMatrix();
			rotateMatrix();
			moveRight();
			rotateMatrix();
			
			
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	private void rotateMatrix() {
		int[][] ret = new int[game.length][game.length];

		for (int i = 0; i < game.length; ++i) {
			for (int j = 0; j < game.length; ++j) {
				ret[i][j] = game[game.length - j - 1][i];
			}
		}
		game = ret;
	}

	@Override
	public void addObserver(Presenter presenter) {
		super.addObserver(presenter);
	}

	@Override
	public boolean doAction(int userCommand) {
		switch (userCommand) {
		case CommandsConsts.UP:
			return moveUp();// UP
		case CommandsConsts.DOWN:
			return moveDown();// DOWN
		case CommandsConsts.LEFT:
			return moveLeft();// LEFT
		case CommandsConsts.RIGHT:
			return moveRight();// RIGHT
		case CommandsConsts.INITIATE: {
			this.initiateMaze();
			return true;
		}
		}
		return false;// NOT LEGAL ACTION.
	}

	private void initiateMaze() {
		this.game = new int[][] { { 2, 2, 0, 4 }, { 4, 0, 0, 4 },
				{ 8, 8, 8, 8 }, { 4, 4, 4, 2 } };
	}

}
