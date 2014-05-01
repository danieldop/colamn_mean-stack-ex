package maze;

import java.util.HashMap;

import model.algorithms.Action;
import model.algorithms.State;

public class MazeAction implements Action<int[]>
{

	private static final HashMap<String,int[]> actionMap;

	public static final String UP = "UP";
	public static final String DOWN = "DOWN";
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	public static final String DIAGONAL_UP_RIGHT = "DIAGONAL_UP_RIGHT";
	public static final String DIAGONAL_UP_LEFT = "DIAGONAL_UP_LEFT";
	public static final String DIAGONAL_DOWN_RIGHT = "DIAGONAL_DOWN_RIGHT";
	public static final String DIAGONAL_DOWN_LEFT = "DIAGONAL_DOWN_LEFT";
	
	static
	{
		actionMap = new HashMap<String,int[]>();
		actionMap.put(UP,new int[]{-1,0});
		actionMap.put(DOWN,new int[]{+1,0});
		actionMap.put(LEFT,new int[]{0,-1});
		actionMap.put(RIGHT,new int[]{0,+1});
		actionMap.put(DIAGONAL_UP_RIGHT,new int[]{-1,+1});
		actionMap.put(DIAGONAL_UP_LEFT,new int[]{-1,-1});
		actionMap.put(DIAGONAL_DOWN_RIGHT,new int[]{+1,+1});
		actionMap.put(DIAGONAL_DOWN_LEFT,new int[]{+1,-1});
	}
	
	private String action;
	public MazeAction(String action)
	{
		this.action = action;
	}
	@Override
	public State<int[]> doAction(int[] stateObj) 
	{
		int[] actionArr = actionMap.get(this.action);//get action to preform from map.
		
		int[] newStateArr = new int[2];//initiate the new state arr.
		
		newStateArr[0] = stateObj[0]+actionArr[0];
		newStateArr[1] = stateObj[1]+actionArr[1];
		
		return new State<int[]>(Maze.toStateStr(newStateArr),newStateArr);
	}

	@Override
	public String getName() {
		return this.action;
	}
}
