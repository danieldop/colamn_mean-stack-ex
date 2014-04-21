package model;

import java.util.Observable;

import consts.CommandsConsts;
import controller.Presenter;

public class MazeModel extends Observable implements Model{

	private int[][] maze;
	public MazeModel()
	{
		
	}

	@Override
	public int[][] getData() {
		
		return maze;
	}
	
	private boolean move(int iTo,int jTo)
	{
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
								maze[i+iTo][j+jTo] = 3;//winning mouse
							else
								maze[i+iTo][j+jTo] = 1;
							maze[i][j] = 0;
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

	@Override
	public boolean doAction(int userCommand) 
	{
		switch(userCommand)
		{
			case CommandsConsts.UP:
				return move(-1,0);//UP
			case CommandsConsts.DOWN:
				return move(1,0);//DOWN
			case CommandsConsts.LEFT:
				return move(0,-1);//LEFT
			case CommandsConsts.RIGHT:
				return move(0,1);//RIGHT
			case CommandsConsts.UP_LEFT:
				return move(-1,-1);//DIAGONAL_UP_LEFT
			case CommandsConsts.UP_RIGHT:
				return move(-1,1);//DIAGONAL_UP_RIGHT
			case CommandsConsts.DOWN_LEFT:
				return move(1,-1);//DIAGONAL_DOWN_LEFT
			case CommandsConsts.DOWN_RIGHT:
				return move(1,1);//DIAGONAL_DOWN_RIGHT
			case CommandsConsts.INITIATE:
			{
				this.initiateMaze();
				return true;
			}
		}
		return false;//NOT LEGAL ACTION.
	}

	private void initiateMaze() 
	{
		this.maze = new int[][]
				{
				{1,0,0,0,0}
				,{0,-1,-1,0,-1}
				,{0,0,0,0,0}
				,{-1,0,-1,-1,-1}
				,{0,0,0,0,2}
				};
	}

}
