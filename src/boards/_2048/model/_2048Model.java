package boards._2048.model;

import java.util.Random;
import java.util.Stack;

import mvp.consts.CommandsConsts;
import mvp.interfaces.model.ModelAbs;
import boards._2048.model.objects._2048State;
//import static java.lang.System.out;
import filesys.FileHandler;

public class _2048Model extends ModelAbs {

	private Stack<_2048State> undoList;

	public _2048Model() {

	}

	public _2048Model(int size) {
		this.rows = this.cols = size;
	}

	private boolean makeStep(boolean isCheckOnly) { // Make a right step
		boolean res = false;

		for (int i = 0; i < board.length; i++) {
			for (int j = board.length - 1; j > 0; j--) {
				if (board[i][j] > 1 && board[i][j] == board[i][j - 1]) {
					if (isCheckOnly) {

						return true;
					}
					res = true;
					score += (2 * board[i][j]);
					board[i][j] = 2 * board[i][j];
					board[i][j - 1] = -1;
				} else if (board[i][j] == 0 && board[i][j - 1] != 0) {
					if (isCheckOnly) {
						return true;
					}
					res = true;
					board[i][j] = board[i][j - 1];
					board[i][j - 1] = 0;
					if (j < board.length - 1) {
						j += 2;
					}
				}
			}
			for (int j = board.length - 1; j >= 0; j--) {
				if (board[i][j] == -1) {
					board[i][j] = 0;
				}
				while (j > 0 && board[i][j] < 2 && board[i][j - 1] > 0) {
					board[i][j] = board[i][j - 1];
					board[i][j - 1] = 0;
				}
			}

		}
		if (res) {
			// adding a random tile after a successful move
			addRandomTile();
		}

		return res;

	}

	private void addRandomTile() { // Randomly adding a tile
		Random rand = new Random();
		while (true) {
			int choice1 = rand.nextInt(board.length);
			int choice2 = rand.nextInt(board.length);
			if (board[choice1][choice2] == 0) {
				int tile = rand.nextInt(9);
				if (tile == 6) {
					board[choice1][choice2] = 4;
				} else {
					board[choice1][choice2] = 2;
				}
				break;
			}

		}
	}

	//
	// Is game ended function.
	//
	private void isEndGame() {

		if (!this.isWin) {
			for (int[] arr : board) {
				for (int elem : arr) {
					if (elem == 2048) {
						this.isWin = true;
						return;
					}
				}
			}
		}

		// first we check if any empty tiles exists
		for (int[] arr : board) {
			for (int elem : arr) {
				if (elem == 0) {
					return;
				}
			}
		}
		// if not, we check if optional moves are available
		if (generalMove(1, 3, true) || generalMove(3, 1, true)
				|| generalMove(2, 2, true) || generalMove(0, 0, true)) {
			return;
		}
		// if we got till here, we lost
		this.isLost = true;

	}

	//
	// Rotating a matrix 90 degree clockwise
	//
	private void rotateMatrix(int count) {
		for (int t = 0; t < count; t++) {
			int[][] ret = new int[board.length][board.length];
			for (int i = 0; i < board.length; ++i) {
				for (int j = 0; j < board.length; ++j) {
					ret[i][j] = board[board.length - j - 1][i];
				}
			}
			board = ret;
		}

	}

	//
	// General move perform a right step to the 2048 board. (for example: if
	// it's an "Up move" we
	// first rotate it once, then perform a right step, and then rotate it again
	// 3 times.
	//
	private boolean generalMove(int firstRotate, int secondRotate,
			boolean isCheckOnly) {
		if (!isCheckOnly)// first(if not check only) we push current state(no
							// matter if valid move)
			this.undoList.push(new _2048State(board, score));

		rotateMatrix(firstRotate);
		boolean res = makeStep(isCheckOnly);
		rotateMatrix(secondRotate);
		if (isCheckOnly) {
			return res;
		} else if (!res)// if not valid.
			this.undoList.pop();

		isEndGame();
		return true;
	}

	@Override
	public boolean doMovement(int userCommand) {

		switch (userCommand) {
		case CommandsConsts.VIEW_MOVE_UP:
			return generalMove(1, 3, false);
		case CommandsConsts.VIEW_MOVE_DOWN:
			return generalMove(3, 1, false);
		case CommandsConsts.VIEW_MOVE_LEFT:
			return generalMove(2, 2, false);
		case CommandsConsts.VIEW_MOVE_RIGHT:
			return generalMove(0, 0, false);
		case CommandsConsts.VIEW_MOVE_UNDO:
			return undoMove();
		}
		return false;// NOT LEGAL ACTION.
	}

	@Override
	public boolean doTecAction(int userCommand, Object dataHelper) {
		switch (userCommand) {
		case CommandsConsts.VIEW_TEC_INITIATE_BOARD:
		case CommandsConsts.VIEW_TEC_RESTART_BOARD:
			this.initiateBoard(this.rows);
			return true;
		case CommandsConsts.VIEW_TEC_BOARD_PREVIEW:
			set2048Preview(dataHelper);
			return true;
		case CommandsConsts.VIEW_TEC_LOAD_BOARD:
			try {
				_2048Model m = (_2048Model) FileHandler.loadObject(dataHelper
						.toString());// dataHelper should be the path.
				this.load(m);
				return true;
			} catch (Exception e) {
				return false;
			}
		case CommandsConsts.VIEW_TEC_SAVE_BOARD:
			try {
				FileHandler.saveObject(save(), dataHelper.toString());// dataHelper
																		// should
																		// be
																		// the
																		// path
			} catch (Exception e) {
				return false;
			}
			return true;
		case CommandsConsts.VIEW_TEC_IS_WON_GAME: {
			if (this.isWin)
				this.execCommand(CommandsConsts.MODEL_SHOW_WON_GAME);
			return false;
		}
		case CommandsConsts.VIEW_TEC_IS_LOST_GAME: {
			if (this.isLost)
				this.execCommand(CommandsConsts.MODEL_SHOW_LOST_GAME);
			return false;
		}
		}
		return false;// NOT LEGAL ACTION.
	}

	private void set2048Preview(Object dataHelper) {
		// dataHelper = rows and columns

		this.board = new int[(Integer) dataHelper][(Integer) dataHelper];

	}

	@Override
	protected boolean undoMove() {
		if (this.undoList.isEmpty())
			return false;

		_2048State prevState = this.undoList.pop();
		this.board = prevState.getBoardState();
		this.score = prevState.getScore();

		return true;

	}

	private void initiateBoard(int size) {

		this.board = new int[size][size];
		this.undoList = new Stack<_2048State>();
		this.score = 0;
		this.isWin = false;
		this.isLost = false;
		addRandomTile();
		addRandomTile();
	}

	@Override
	protected _2048Model save() {
		_2048Model m = new _2048Model(4);
		m.load(this);
		return m;
	}

	private void load(_2048Model m) {
		this.board = m.board;
		this.score = m.score;
		this.undoList = m.undoList;
		this.isWin = m.isWin;
		this.isLost = m.isLost;
		this.rows = m.rows;
		this.cols = m.cols;
	}
}
