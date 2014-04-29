package model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

//import static java.lang.System.out;
import consts.CommandsConsts;
import controller.Presenter;

public class Model2048 extends Observable implements Model {

	private int[][] game;
	private ArrayList<int[][]> undoList;

	public Model2048() {

	}

	@Override
	public int[][] getData() {
		return game;
	}

	private boolean makeStep(boolean isCheckOnly) {
		boolean res = false;

		for (int i = 0; i < game.length; i++) {
			for (int j = game.length - 1; j > 0; j--) {
				if (game[i][j] > 1 && game[i][j] == game[i][j - 1]) {
					if (isCheckOnly) {

						return true;
					}
					res = true;
					game[i][j] = 2 * game[i][j];
					game[i][j - 1] = -1;
				} else if (game[i][j] == 0 && game[i][j - 1] != 0) {
					if (isCheckOnly) {
						return true;
					}
					res = true;
					game[i][j] = game[i][j - 1];
					game[i][j - 1] = 0;
					if (j < game.length - 1) {
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
		if (res) {
			addRandomTile();
		}

		return res;

	}

	private boolean moveRight(boolean isCheckOnly) {
		undoList.add(game);
		boolean res = makeStep(isCheckOnly);

		if (isCheckOnly) {
			return res;
		} else {
			if (res) {
				//undoList.add(game);
			}
		}

		isEndGame();
		return true;
	}

	private boolean moveLeft(boolean isCheckOnly) {
		undoList.add(game);
		rotateMatrix(2);
		boolean res = makeStep(isCheckOnly);
		rotateMatrix(2);

		if (isCheckOnly) {
			return res;
		} else {
			if (res) {
//				undoList.add(game);
			}
		}

		isEndGame();
		return true;
	}

	private boolean moveUp(boolean isCheckOnly) {
		undoList.add(game);
		rotateMatrix(1);
		boolean res = makeStep(isCheckOnly);
		rotateMatrix(3);
		if (isCheckOnly) {
			return res;
		} else {
			if (res) {
//				undoList.add(game);
			}
		}
		isEndGame();
		return true;
	}

	private boolean moveDown(boolean isCheckOnly) {
		undoList.add(game);
		rotateMatrix(3);
		boolean res = makeStep(isCheckOnly);
		rotateMatrix(1);
		if (isCheckOnly) {
			return res;
		} else {
			if (res) {
			//	undoList.add(game);
			}
		}

		isEndGame();
		return true;

	}

	private void addRandomTile() {
		Random rand = new Random();
		while (true) {
			int choice1 = rand.nextInt(game.length);
			int choice2 = rand.nextInt(game.length);
			if (game[choice1][choice2] == 0) {
				int tile = rand.nextInt(10);
				if (tile % 4 == 0) {
					game[choice1][choice2] = 4;
				} else {
					game[choice1][choice2] = 2;
				}
				break;
			}

		}
	}

	private boolean isEndGame() {

		for (int[] arr : game) {
			for (int elem : arr) {
				if (elem == 0) {
					return false;
				}
			}
		}
		if (moveUp(true) || moveDown(true) || moveLeft(true) || moveRight(true)) {
			return false;
		}

		return true;
	}

	private void rotateMatrix(int count) {
		for (int t = 0; t < count; t++) {
			int[][] ret = new int[game.length][game.length];
			for (int i = 0; i < game.length; ++i) {
				for (int j = 0; j < game.length; ++j) {
					ret[i][j] = game[game.length - j - 1][i];
				}
			}
			game = ret;
		}

	}

	@Override
	public void addObserver(Presenter presenter) {
		super.addObserver(presenter);
	}

	@Override
	public boolean doAction(int userCommand) {
		try {
			switch (userCommand) {
			case CommandsConsts.UP:
				return moveUp(false);// UP
			case CommandsConsts.DOWN:
				return moveDown(false);// DOWN
			case CommandsConsts.LEFT:
				return moveLeft(false);// LEFT
			case CommandsConsts.RIGHT:
				return moveRight(false);// RIGHT
			case 8:
				UndoStep();
				return true;
			case CommandsConsts.INITIATE: {
				this.initiateMaze(4);
				return true;
			}
			}
			return false;// NOT LEGAL ACTION.
		} catch (Exception e) {
			return false;
		}
	}

	private void UndoStep() {
		this.game = this.undoList.get(this.undoList.size() - 1);
		this.undoList.remove(this.undoList.size() - 1);
	}

	private void initiateMaze(int size) {

		this.game = new int[size][size];
		this.undoList = new ArrayList<int[][]>();
		addRandomTile();
		addRandomTile();
	}

}
