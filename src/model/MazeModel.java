package model;

import java.util.Observable;
import java.util.Random;
import java.util.Stack;

import maze.Maze;
import maze.MazeDomain;
import model.algorithms.Action;
import model.algorithms.Distance;
import model.algorithms.State;
import model.algorithms.bfs.BFS;
import consts.CommandsConsts;
import controller.Presenter;
import filesys.FileHandler;

public class MazeModel extends Observable implements Model
{
	private int[][] initiatedMaze;//incase we press restart.
	private int[][] maze;
	private int score = 0;
	private Stack<Integer> moveHistory;
	private boolean isWin;
	private int level;
	public MazeModel()
	{
		this.moveHistory = new Stack<Integer>();
		this.score = 0;
	}

	public MazeModel(int rows,int cols,int level)
	{
		this.level = level;//for now its hardcoded.
		
		generateMaze(rows, cols);
		this.moveHistory = new Stack<Integer>();
		this.score = 0;
	}
	
	private void generateMaze(int rows,int cols) 
	{
		
		
		this.initiatedMaze = new int[rows][cols];
		this.maze = new int[rows][cols];
		
		int distance = level*2;
		
		if(distance>=rows)
		{
			distance = rows/2;
		}
		if(distance == 0 )
			distance = 1;
		
		Random random = new Random();
		int[] mouseCoords = new int[]{0,random.nextInt(cols)};
		int[] cheeseCoords = new int[]{random.nextInt(distance)+distance-1,random.nextInt(cols)};
		
		if(mouseCoords[0] == cheeseCoords[0] && mouseCoords[1] == cheeseCoords[1])//handle the only 
		{
			if(cols == 1)//not suppost to allow it... but lets make is generic
			{
				if(rows==1)//silly..
				{
					this.initiatedMaze[mouseCoords[0]][mouseCoords[1]] = 3;//winner..
					return;
				}			
				mouseCoords[0] = 0;
				cheeseCoords[0] = rows-1;	
			}
			mouseCoords[1] = 0;
			cheeseCoords[1] = cols-1;
		}
		
		this.initiatedMaze[mouseCoords[0]][mouseCoords[1]] = 1;
		this.initiatedMaze[cheeseCoords[0]][cheeseCoords[1]] = 2;
		
		createDefaultPath(rows,cols,cheeseCoords,mouseCoords);
		
	}

	private void initWalls()
	{
		int maxNum = level*5 + 20,maxWalls = maxNum*(initiatedMaze.length)*(initiatedMaze[0].length)/100;
		int wallsCount = 0,j,num;
		Random rand = new Random();
		for(int i=0;i<this.initiatedMaze.length;i++)
		{
			for(j=0;j<this.initiatedMaze[0].length;j++)
			{
				if(this.initiatedMaze[i][j]==0)
				{
					num = rand.nextInt(99)+1;
					if(num<maxNum+1)
					{
						this.initiatedMaze[i][j] = -1;
						
						if(wallsCount == maxWalls)
							break;
					}
				}
			}
			if(wallsCount == maxWalls)
				break;
		}
	}

	private void createDefaultPath(final int rows,final int cols,int[] cheeseCoords,int[] mouseCoords) 
	{
		initWalls();//first put walls randomly
		
		
		Maze maze= new Maze(rows,cols,new int[][]{},cheeseCoords,mouseCoords); 
		BFS<int[]> bfs =new BFS<int[]>
		( 
				new MazeDomain( maze )
				,new Distance<int[]>() 
				{//G DISTANCE CALCULATOR
					@Override
					public double getDistance(State<int[]> from, State<int[]> to) 
					{
						return Math.abs((Math.random()*from.getG())+from.getG()-to.getG());
					}
				}
		);
		
		int[] currObj;
		State<int[]> curr = maze.getStartState();
		
		for(Action<int[]> a:bfs.search())
		{
			curr = a.doAction(curr.getObj());
			
		
			currObj = curr.getObj();
			if(cheeseCoords[0] != currObj[0] || cheeseCoords[1] != currObj[1])
				this.initiatedMaze[currObj[0]][currObj[1]] = 0;
		}
	}

	@Override
	public int[][] getData() 
	{
		return maze;
	}
	
	private void load(MazeModel m)
	{
		this.maze = m.maze;
		this.initiatedMaze = m.initiatedMaze;
		this.score = m.score;
		this.moveHistory = m.moveHistory;
		this.isWin = m.isWin;
	}
	
	private MazeModel copy()//THIS IS SO WE DONT SAVE THE OBS OBJECT CONTAINING THE OBSERVERS.
	{
		MazeModel m = new MazeModel();
		m.load(this);
		return m;
	}
	
	private boolean move(int iTo,int jTo,int scoreTo, int userCommand,boolean isUndo)
	{
		if(isUndo)
		{
			iTo = iTo*-1;
			jTo = jTo*-1;
			scoreTo = scoreTo*-1;
			
			if(this.isWin)
				return undoWinMove(iTo,jTo,scoreTo,userCommand);
		}
		for(int i=0;i<maze.length;i++)
		{
			for(int j=0;j<maze[i].length;j++)
			{
				if(maze[i][j] == 1)
				{
					if(i+iTo>-1 && i+iTo<maze.length && j+jTo>-1 && j+jTo<maze[i+iTo].length)//range check..
					{		
						if(maze[i+iTo][j+jTo]!=-1)
						{
							if(maze[i+iTo][j+jTo] == 2)
							{
								maze[i+iTo][j+jTo] = 3;//winning mouse
								this.isWin = true;
							}
							else
								maze[i+iTo][j+jTo] = 1;
							
							maze[i][j] = 0;
							score+=scoreTo;
							
							if(!isUndo)
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

	private boolean undoWinMove(int iTo, int jTo, int scoreTo, int userCommand) 
	{
		for(int i=0;i<maze.length;i++)
		{
			for(int j=0;j<maze[i].length;j++)
			{
				if(maze[i][j] == 3)
				{
					if(i+iTo>-1 && i+iTo<maze.length && j+jTo>-1 && j+jTo<maze[i+iTo].length)//range check..
					{		
						if(maze[i+iTo][j+jTo]!=-1)
						{
							this.isWin = false;//reset the winning.
							
							maze[i+iTo][j+jTo] = 1;//reset the mouse.
							
							maze[i][j] = 2;//resetting the cheese..
							
							score+=scoreTo;//undo the score
							
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
	public void addObserver(Presenter presenter) 
	{
		super.addObserver(presenter);
	}

	private boolean doMovement(int userCommand,boolean isUndo)//isUndo: 1/-1
	{
		switch(userCommand)
		{
			case CommandsConsts.VIEW_MOVE_UP:
				return move(-1,0,10,userCommand,isUndo);
			case CommandsConsts.VIEW_MOVE_DOWN:
				return move(1,0,10,userCommand,isUndo);
			case CommandsConsts.VIEW_MOVE_LEFT:
				return move(0,-1,10,userCommand,isUndo);
			case CommandsConsts.VIEW_MOVE_RIGHT:
				return move(0,1,10,userCommand,isUndo);
			case CommandsConsts.VIEW_MOVE_UP_LEFT:
				return move(-1,-1,15,userCommand,isUndo);//DIAGONAL_UP_LEFT
			case CommandsConsts.VIEW_MOVE_UP_RIGHT:
				return move(-1,1,15,userCommand,isUndo);//DIAGONAL_UP_RIGHT
			case CommandsConsts.VIEW_MOVE_DOWN_LEFT:
				return move(1,-1,15,userCommand,isUndo);//DIAGONAL_DOWN_LEFT
			case CommandsConsts.VIEW_MOVE_DOWN_RIGHT:
				return move(1,1,15,userCommand,isUndo);//DIAGONAL_DOWN_RIGHT
			case CommandsConsts.VIEW_MOVE_UNDO:
				return undoMove();
		}
		return false;//NOT LEGAL MOVEMENT.
	}
	
	@Override
	public boolean doMovement(int userCommand) 
	{
		return doMovement(userCommand,false);//for undo behavior lookup: undoMove().
	}

	private boolean undoMove() 
	{
		if(this.moveHistory.isEmpty())
			return false;
		
		return this.doMovement(this.moveHistory.pop(),true);
	}

	private void recordHistory(int userCommand) 
	{
		this.moveHistory.push(userCommand);
	}

	
	@Override
	public int getScore() {
		return this.score;
	}

	@Override
	public boolean doTecAction(int userCommand, Object dataHelper) 
	{
		switch(userCommand)
		{
			case CommandsConsts.VIEW_TEC_INITIATE_BOARD:
			{
				this.restartMaze();
				return true;
			}
			case CommandsConsts.VIEW_TEC_LOAD_BOARD:
			{
				try
				{
					MazeModel m = (MazeModel)FileHandler.loadObject(dataHelper.toString());//dataHelper should be the path.
					this.load(m);
					return true;
				}
				catch(Exception e)
				{
					return false;
				}
			}
			case CommandsConsts.VIEW_TEC_SAVE_BOARD:
			{
				try 
				{
					FileHandler.saveObject(this.copy(),dataHelper.toString());//dataHelper should be the path
				} catch (Exception e) 
				{
					return false;
				}
				return true;
			}
			case CommandsConsts.VIEW_TEC_BOARD_PREVIEW:
			{
				setPreviewMaze(dataHelper);
				return true;
			}
		}
		return false;//NO LEGAL TECHNICAL COMMAND.
	}

	public void restartMaze() 
	{
		this.maze = new int[this.initiatedMaze.length][this.initiatedMaze[0].length];
		int j;
		for(int i=0;i<this.initiatedMaze.length;i++)
		{
			for(j=0;j<this.initiatedMaze[0].length;j++)
			{
				this.maze[i][j] = this.initiatedMaze[i][j];
			}
		}
		this.score = 0;
		this.moveHistory = new Stack<Integer>();
	}

	public void setPreviewMaze(Object dataHelper) 
	{
		Integer[] rowsCols = (Integer[]) dataHelper;//[0] = rows,[1]  = columns
		
		this.maze = new int[rowsCols[0]][rowsCols[1]];
	}

}
