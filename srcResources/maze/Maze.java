package maze;

import model.algorithms.Problem;
import model.algorithms.State;

/*
 * rows represents number of rows.
 * columns represents number of columns.
 * walls => coordinates which represents walls(-1) on the 2d array.
 * mouse => coordinates which represents the mouse.
 * cheese => coordinates which represents the cheese.
 * WALL,CHEESE,MOUSE => constants representing the implementation of each on the 2d array.
 */

public class Maze extends Problem<int[]> 
{
	/**
	 * 
	 */
	private int rowsLength;
	private int columnsLength;
	private int[][] walls;
	private int[] cheese;
	private int[] mouse;
	private final int WALL = -1;
	private final int CHEESE = 2;
	private final int MOUSE = 1;
	private int[][] maze;
	/**
	 * init the maze according to rows and columns.
	 * setting the walls.
	 * setting the cheese.
	 * setting the mouse.
	 */
	public Maze()
	{
		super(null,null);
	}
	public Maze(int rows,int columns,int[][] walls,int[] cheese,int[] mouse)
	{
		super(new State<int[]>(Maze.toStateStr(mouse),new int[]{mouse[0],mouse[1]}),new State<int[]>(Maze.toStateStr(cheese),new int[]{cheese[0],cheese[1]}));
		this.rowsLength = rows;
		this.columnsLength = columns;
		this.walls = walls;
		this.cheese = cheese;
		this.mouse = mouse;
		
		maze = new int[rows][columns];
		for(int i=0;i<walls.length;i++)
		{
			maze[walls[i][0]][walls[i][1]] = WALL;
		}
		maze[cheese[0]][cheese[1]] = CHEESE;
		maze[mouse[0]][mouse[1]] = MOUSE;
	}
	public static String toStateStr(int[] newS)//switching from int[] to the representing state(String state on State class.) 
	{
		return String.valueOf(newS[0])+","+String.valueOf(newS[1]);
	}
	public boolean isWall(int row,int col)//validate square.
	{
		return this.maze[row][col] == -1;
	}
	
	/* ---START
	 * Should be accessible(for the future maybe.).<NOT USED>
	 */
	public int[] getMouse()
	{
		return this.mouse;
	}
	public int[] getCheese()
	{
		return this.cheese;
	}
	public int[][] getWalls()
	{
		return this.walls;
	}
	public int getRowsLength()
	{
		return this.rowsLength;
	}
	public int getColumnsLength()
	{
		return this.columnsLength;
	}
	public boolean isCheese(int row,int col)
	{
		return this.maze[row][col] == 2;
	}
	public boolean isMouse(int row,int col)
	{
		return this.maze[row][col] == 1;
	}
	/*
	 * Should be accessible(for the future maybe.).<NOT USED>
	 * ---END */
}
