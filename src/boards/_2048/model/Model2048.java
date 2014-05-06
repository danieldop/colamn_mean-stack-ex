package boards._2048.model;

import java.util.Random;
import java.util.Stack;

import mvp.consts.CommandsConsts;
import mvp.interfaces.model.ModelAbs;
import boards._2048.model.objects._2048State;
//import static java.lang.System.out;
import filesys.FileHandler;

public class Model2048 extends ModelAbs {

	private Stack<_2048State> undoList;
	
	public Model2048()
	{
		
	}
	
	public Model2048(int size) {
		this.rows = this.cols = size;
	}

	private boolean makeStep(boolean isCheckOnly) {
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
			addRandomTile();
		}

		return res;

	}

	private boolean moveRight(boolean isCheckOnly) {
		undoList.add(new _2048State(board, score));
		rotateMatrix(4);
		boolean res = makeStep(isCheckOnly);

		if (isCheckOnly) {
			return res;
		} else {
			if (res) {
				// undoList.add(game);
			}
		}

		isEndGame();
		return true;
	}

	private boolean moveLeft(boolean isCheckOnly) {
		undoList.add(new _2048State(board, score));
		rotateMatrix(2);
		boolean res = makeStep(isCheckOnly);
		rotateMatrix(2);

		if (isCheckOnly) {
			return res;
		} else {
			if (res) {
				// undoList.add(game);
			}
		}

		isEndGame();
		return true;
	}

	private boolean moveUp(boolean isCheckOnly) {
		undoList.add(new _2048State(board, score));
		rotateMatrix(1);
		boolean res = makeStep(isCheckOnly);
		rotateMatrix(3);
		if (isCheckOnly) {
			return res;
		} else {
			if (res) {
				// undoList.add(game);
			}
		}
		isEndGame();
		return true;
	}

	private boolean moveDown(boolean isCheckOnly) {
		undoList.add(new _2048State(board, score));
		rotateMatrix(3);
		boolean res = makeStep(isCheckOnly);
		rotateMatrix(1);
		if (isCheckOnly) {
			return res;
		} else {
			if (res) {
				// undoList.add(game);
			}
		}

		isEndGame();
		return true;

	}

	private void addRandomTile() {
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

	private boolean isEndGame() {

		for (int[] arr : board) {
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
			int[][] ret = new int[board.length][board.length];
			for (int i = 0; i < board.length; ++i) {
				for (int j = 0; j < board.length; ++j) {
					ret[i][j] = board[board.length - j - 1][i];
				}
			}
			board = ret;
		}

	}

	@Override
	public boolean doMovement(int userCommand) {
		
			switch (userCommand) 
			{
				case CommandsConsts.VIEW_MOVE_UP:
					return moveUp(false);// UP
				case CommandsConsts.VIEW_MOVE_DOWN:
					return moveDown(false);// DOWN
				case CommandsConsts.VIEW_MOVE_LEFT:
					return moveLeft(false);// LEFT
				case CommandsConsts.VIEW_MOVE_RIGHT:
					return moveRight(false);// RIGHT
				case CommandsConsts.VIEW_MOVE_UNDO:
					return undoMove();
			}
			return false;// NOT LEGAL ACTION.
	}
	
	@Override
	public boolean doTecAction(int userCommand, Object dataHelper)
	{
		switch (userCommand) 
		{
			case CommandsConsts.VIEW_TEC_INITIATE_BOARD:
			case CommandsConsts.VIEW_TEC_RESTART_BOARD:
				this.initiateBoard(this.rows);
				return true;
			case CommandsConsts.VIEW_TEC_BOARD_PREVIEW:
				break;//TODO: COMPLETE THIS.
			case CommandsConsts.VIEW_TEC_LOAD_BOARD:
			try
			{
				Model2048 m = (Model2048)FileHandler.loadObject(dataHelper.toString());//dataHelper should be the path.
				this.load(m);
				return true;
			}
			catch(Exception e)
			{
				return false;
			}
			case CommandsConsts.VIEW_TEC_SAVE_BOARD:
			try 
			{
				FileHandler.saveObject(save(),dataHelper.toString());//dataHelper should be the path
			} catch (Exception e) 
			{
				return false;
			}
			return true;
		}
		return false;// NOT LEGAL ACTION.
	}

	@Override
	protected boolean undoMove() 
	{
		if(this.undoList.size()==0)
			return false;
		_2048State prevState = this.undoList.pop();
		this.board = prevState.getBoardState();
		this.score = prevState.getScore();
		return true;
		// this.game = this.undoList.get(this.undoList.size() - 1);
		// this.undoList.remove(this.undoList.size());
	}

	private void initiateBoard(int size) {

		this.board = new int[size][size];
		this.undoList = new Stack<_2048State>();
		addRandomTile();
		addRandomTile();

	}
	@Override
	protected Model2048 save()
	{
		Model2048 m = new Model2048(4);
		m.load(this);
		return m;
	}

	private void load(Model2048 m)
	{
		this.board = m.board;
		this.score = m.score;
		this.undoList = m.undoList;
		this.isWin = m.isWin;
		this.rows = m.rows;
		this.cols = m.cols;
	}
}
