package boards.maze.model;

import java.util.Observable;
import java.util.Random;
import java.util.Stack;

import maze.Maze;
import maze.MazeDomain;
import model.algorithms.Action;
import model.algorithms.Distance;
import model.algorithms.State;
import model.algorithms.bfs.BFS;
import mvp.consts.CommandsConsts;
import mvp.controller.Presenter;
import mvp.interfaces.model.Model;
import mvp.interfaces.model.ModelAbs;
import filesys.FileHandler;

public class MazeModel extends ModelAbs {
	private int[][] initiatedMaze;// incase we press restart.
	private int[][] maze;
	private int score = 0;
	private Stack<Integer> moveHistory;
	private boolean isWin;

	private int rows, cols, level;

	public MazeModel() {
		// DEFAULT SETTINGS.

		this.moveHistory = new Stack<Integer>();
		this.score = 0;
		this.isWin = false;
		this.rows = 1;
		this.cols = 1;
		this.level = 1;
	}

	public MazeModel(final int rows, final int cols, final int level) {
		this.moveHistory = new Stack<Integer>();
		this.score = 0;
		this.isWin = false;

		this.rows = rows;
		this.cols = cols;
		this.level = level;
	}

	public void initiateMaze() {
		this.initiatedMaze = new int[rows][cols];
		this.maze = new int[rows][cols];
		new Thread(new Runnable() {

			@Override
			public void run() {
				generateMaze(rows, cols, level);
				notifyPresenters(CommandsConsts.MODEL_SHOW_GENERATED_MAZE);
			}
		}).start();
	}

	/**
	 * 
	 * @param rows
	 * @param cols
	 * @param level
	 *            Generating a random maze based on rows,cols and level.
	 */

	private void notifyPresenters(int command) {
		setModelCommand(command);
		setChanged();
		notifyObservers();
	}

	private void generateMaze(int rows, int cols, int level) {
		Random random = new Random();

		int mouseRow = random.nextInt(rows) - level * level;
		int cheeseRow = random.nextInt(rows) + level * level;

		if (mouseRow < 0)// mouse out of boundaries?
			mouseRow = 0;
		if (cheeseRow > rows - 1)// cheese out of boundaries?
			cheeseRow = rows - 1;

		int[] mouseCoords = new int[] { mouseRow, random.nextInt(cols) };
		int[] cheeseCoords = new int[] { cheeseRow, random.nextInt(cols) };

		if (mouseCoords[0] == cheeseCoords[0]
				&& mouseCoords[1] == cheeseCoords[1])// handle the only
		{
			if (cols == 1)// not suppose to allow it... but lets make it generic
			{
				if (rows == 1)// silly..
				{
					this.initiatedMaze[mouseCoords[0]][mouseCoords[1]] = 3;// winner..
					return;
				}
				mouseCoords[0] = 0;
				cheeseCoords[0] = rows - 1;
			}
			mouseCoords[1] = 0;
			cheeseCoords[1] = cols - 1;
		}

		this.initiatedMaze[mouseCoords[0]][mouseCoords[1]] = 1;
		this.initiatedMaze[cheeseCoords[0]][cheeseCoords[1]] = 2;

		createDefaultPath(rows, cols, cheeseCoords, mouseCoords, level);
		restartMaze();
	}

	/**
	 * 
	 * @param level
	 *            generating maze walla based on level specified.
	 */
	private void initWalls(int level) {
		int maxNum = level * 5 + 25, maxWalls = maxNum * (initiatedMaze.length)
				* (initiatedMaze[0].length) / 100;
		int wallsCount = 0, j, num;
		Random rand = new Random();
		for (int i = 0; i < this.initiatedMaze.length; i++) {
			for (j = 0; j < this.initiatedMaze[0].length; j++) {
				if (this.initiatedMaze[i][j] == 0) {
					num = rand.nextInt(99) + 1;
					if (num < maxNum + 1) {
						this.initiatedMaze[i][j] = -1;

						if (wallsCount == maxWalls)
							break;
					}
				}
			}
			if (wallsCount == maxWalls)
				break;
		}
	}

	/**
	 * 
	 * @param rows
	 * @param cols
	 * @param cheeseCoords
	 * @param mouseCoords
	 * @param level
	 * 
	 *            Based on the mouse and cheese location, finding a default path
	 *            from mouse to the cheese. afterwards setting the path on the
	 *            board (in case walls are covering the path.)
	 */
	private void createDefaultPath(final int rows, final int cols,
			int[] cheeseCoords, int[] mouseCoords, int level) {
		initWalls(level);// first put walls randomly

		Maze maze = new Maze(rows, cols, new int[][] {}, cheeseCoords,
				mouseCoords);
		BFS<int[]> bfs = new BFS<int[]>(new MazeDomain(maze),
				new Distance<int[]>() {// G DISTANCE CALCULATOR
					@Override
					public double getDistance(State<int[]> from, State<int[]> to) {
						return Math.abs((Math.random() * (from.getG() - to
								.getG())));
					}
				});

		int[] currObj;
		State<int[]> curr = maze.getStartState();

		for (Action<int[]> a : bfs.search()) {
			curr = a.doAction(curr.getObj());

			currObj = curr.getObj();
			if (cheeseCoords[0] != currObj[0] || cheeseCoords[1] != currObj[1])
				this.initiatedMaze[currObj[0]][currObj[1]] = 0;
		}
	}

	@Override
	public int[][] getData() {
		return maze;
	}

	/**
	 * 
	 * @param m
	 *            takes the mazeModel specified and setting this mazeModel data
	 *            accordingly
	 */
	private void load(MazeModel m) {
		this.maze = m.maze;
		this.initiatedMaze = m.initiatedMaze;
		this.score = m.score;
		this.moveHistory = m.moveHistory;
		this.isWin = m.isWin;
		this.rows = m.rows;
		this.cols = m.cols;
		this.level = m.level;
	}

	/**
	 * 
	 * @return new mazeModel instance, just like 'this' without the observers.
	 *         *(we don't want to save this object with the observers attached
	 *         to it).
	 */

	/**
	 * 
	 * @param iTo
	 * @param jTo
	 * @param scoreTo
	 * @param userCommand
	 * @param isUndo
	 * @return true if move is valid
	 * 
	 *         makes a move(if valid) records the move on the history moves. if
	 *         undo, each coordinate(iTo,jTo) will be multiplied by -1 to get
	 *         the opposite direction effect.
	 */
	private boolean move(int iTo, int jTo, int scoreTo, int userCommand,
			boolean isUndo) {
		if (isUndo) {
			iTo = iTo * -1;
			jTo = jTo * -1;
			scoreTo = scoreTo * -1;

			if (this.isWin)
				return undoWinMove(iTo, jTo, scoreTo);
		}
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (maze[i][j] == 1) {
					if (i + iTo > -1 && i + iTo < maze.length && j + jTo > -1
							&& j + jTo < maze[i + iTo].length)// range check..
					{
						if (maze[i + iTo][j + jTo] != -1) {
							if (maze[i + iTo][j + jTo] == 2) {
								maze[i + iTo][j + jTo] = 3;// winning mouse
								this.isWin = true;
							} else
								maze[i + iTo][j + jTo] = 1;

							maze[i][j] = 0;
							score += scoreTo;

							if (!isUndo)
								recordHistory(userCommand);

							return true;
						}
					}
					return false;
				}
			}
		}

		return false;
	}

	/**
	 * 
	 * @param iTo
	 * @param jTo
	 * @param scoreTo
	 * @param userCommand
	 * @return when user wins the board will store the data as '3' where the
	 *         cheese('2') used to be. to undo we set the '3'=>'2', again, and
	 *         according to iTo,jTo we set the mouse('1') again.
	 */
	private boolean undoWinMove(int iTo, int jTo, int scoreTo) {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (maze[i][j] == 3) {
					if (i + iTo > -1 && i + iTo < maze.length && j + jTo > -1
							&& j + jTo < maze[i + iTo].length)// range check..
					{
						if (maze[i + iTo][j + jTo] != -1) {
							this.isWin = false;// reset the winning.

							maze[i + iTo][j + jTo] = 1;// reset the mouse.

							maze[i][j] = 2;// resetting the cheese..

							score += scoreTo;// undo the score

							return true;
						}
					}
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public void addObserver(Presenter presenter) {
		super.addObserver(presenter);
	}

	/**
	 * 
	 * @param userCommand
	 * @param isUndo
	 * @return calling the "move" method with parameters according to
	 *         userCommand,isUndo params.
	 */
	private boolean doMovement(int userCommand, boolean isUndo) {
		switch (userCommand) {
		case CommandsConsts.VIEW_MOVE_UP:
			return move(-1, 0, 10, userCommand, isUndo);
		case CommandsConsts.VIEW_MOVE_DOWN:
			return move(1, 0, 10, userCommand, isUndo);
		case CommandsConsts.VIEW_MOVE_LEFT:
			return move(0, -1, 10, userCommand, isUndo);
		case CommandsConsts.VIEW_MOVE_RIGHT:
			return move(0, 1, 10, userCommand, isUndo);
		case CommandsConsts.VIEW_MOVE_UP_LEFT:
			return move(-1, -1, 15, userCommand, isUndo);// DIAGONAL_UP_LEFT
		case CommandsConsts.VIEW_MOVE_UP_RIGHT:
			return move(-1, 1, 15, userCommand, isUndo);// DIAGONAL_UP_RIGHT
		case CommandsConsts.VIEW_MOVE_DOWN_LEFT:
			return move(1, -1, 15, userCommand, isUndo);// DIAGONAL_DOWN_LEFT
		case CommandsConsts.VIEW_MOVE_DOWN_RIGHT:
			return move(1, 1, 15, userCommand, isUndo);// DIAGONAL_DOWN_RIGHT
		case CommandsConsts.VIEW_MOVE_UNDO:
			return undoMove();
		}
		return false;// NOT LEGAL MOVEMENT.
	}

	@Override
	public boolean doMovement(int userCommand) {
		return doMovement(userCommand, false);// for undo behavior lookup:
												// undoMove().
	}

	private void recordHistory(int userCommand) {
		this.moveHistory.push(userCommand);
	}

	@Override
	public int getScore() {
		return this.score;
	}

	@Override
	public boolean doTecAction(int userCommand, Object dataHelper) {
		switch (userCommand) {
		case CommandsConsts.VIEW_TEC_INITIATE_BOARD: {
			this.initiateMaze();
			return true;
		}
		case CommandsConsts.VIEW_TEC_RESTART_BOARD: {
			this.restartMaze();
			return true;
		}
		case CommandsConsts.VIEW_TEC_LOAD_BOARD: {
			try {
				MazeModel m = (MazeModel) FileHandler.loadObject(dataHelper
						.toString());// dataHelper should be the path.
				this.load(m);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		case CommandsConsts.VIEW_TEC_SAVE_BOARD: {
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
		}
		case CommandsConsts.VIEW_TEC_BOARD_PREVIEW: {
			setPreviewMaze(dataHelper);
			return true;
		}
		case CommandsConsts.VIEW_TEC_IS_WON_GAME:
			if (this.isWin) {
				this.isWin = false;
				this.execCommand(CommandsConsts.MODEL_SHOW_WON_GAME);
			}
			return false;
		}
		return false;// NO LEGAL TECHNICAL COMMAND.
	}

	/**
	 * this.maze will now be exactly as this.initiatedMaze; the score will be
	 * restarted,the moveHistory too, and the isWin parameter will be set to
	 * false.
	 */
	public void restartMaze() {
		if (initiatedMaze == null)
			return;
		this.maze = new int[this.initiatedMaze.length][this.initiatedMaze[0].length];
		int j;
		for (int i = 0; i < this.initiatedMaze.length; i++) {
			for (j = 0; j < this.initiatedMaze[0].length; j++) {
				this.maze[i][j] = this.initiatedMaze[i][j];
			}
		}
		this.score = 0;
		this.moveHistory = new Stack<Integer>();
		this.isWin = false;
	}

	/**
	 * 
	 * @param dataHelper
	 *            dataHelper is suppost to be an Integer[] object. it must
	 *            containg 2 positive numbers, these numbers will be rows and
	 *            columns for the 'maze' object.
	 */
	public void setPreviewMaze(Object dataHelper) {
		Integer[] rowsCols = (Integer[]) dataHelper;// [0] = rows,[1] = columns

		this.maze = new int[rowsCols[0]][rowsCols[1]];
	}

	private void setModelCommand(int modelCommand) {
		this.modelCommand = modelCommand;
	}

	@Override
	public int getModelCommand() {
		return this.modelCommand;
	}

	@Override
	protected boolean undoMove() {
		if (this.moveHistory.isEmpty())
			return false;

		return this.doMovement(this.moveHistory.pop(), true);
	}

	@Override
	protected ModelAbs save() {
		MazeModel m = new MazeModel();
		m.load(this);
		return m;
	}

}
