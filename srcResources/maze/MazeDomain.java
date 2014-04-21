package maze;

import java.util.ArrayList;

import model.algorithms.AbsDomain;
import model.algorithms.Action;
import model.algorithms.State;

public class MazeDomain extends AbsDomain<int[],Maze>
{
	public MazeDomain()
	{
		super(null);
	}
	public MazeDomain(Maze m)
	{
		super(m);
	}

	@Override
	public double neighborsDifference(State<int[]> q, State<int[]> qTag) 
	{
		if(q.getObj()[0]-qTag.getObj()[0]!=0 &&q.getObj()[1]-qTag.getObj()[1]!=0)
			return 15;
		return 10;
	}
	
	@Override
	public ArrayList<Action<int[]>> getActions(State<int[]> s) 
	{
		ArrayList<Action<int[]>> actions = new ArrayList<Action<int[]>>();
		int[] pos = s.getObj();
		
		//calculate range
		boolean downFlag = pos[0]+1<this.problem.getRowsLength();
		boolean upFlag = pos[0]>0;
		boolean leftFlag = pos[1]>0;
		boolean rightFlag = pos[1]+1<this.problem.getColumnsLength();
		//end calculate range
		
		//set legal moves
		if(leftFlag && !this.problem.isWall(pos[0],pos[1]-1))
			actions.add(new MazeAction(MazeAction.LEFT));
		if(rightFlag && !this.problem.isWall(pos[0],pos[1]+1))
			actions.add(new MazeAction(MazeAction.RIGHT));
		if(downFlag)
		{
			if(!this.problem.isWall(pos[0]+1,pos[1]))
				actions.add(new MazeAction(MazeAction.DOWN));
			if(leftFlag && !this.problem.isWall(pos[0]+1,pos[1]-1))
				actions.add(new MazeAction(MazeAction.DIAGONAL_DOWN_LEFT));
			if(rightFlag && !this.problem.isWall(pos[0]+1,pos[1]+1))
				actions.add(new MazeAction(MazeAction.DIAGONAL_DOWN_RIGHT));
		}
		if(upFlag)
		{
			if(!this.problem.isWall(pos[0]-1,pos[1]))
				actions.add(new MazeAction(MazeAction.UP));
			if(leftFlag && !this.problem.isWall(pos[0]-1,pos[1]-1))
				actions.add(new MazeAction(MazeAction.DIAGONAL_UP_LEFT));
			if(rightFlag && !this.problem.isWall(pos[0]-1,pos[1]+1))
				actions.add(new MazeAction(MazeAction.DIAGONAL_UP_RIGHT));
		}
		//end set legal moves
		return actions;
	}

	@Override
	public State<int[]> getStartState() {
		// TODO Auto-generated method stub
		return this.problem.getStartState();
	}

	@Override
	public State<int[]> getGoalState() {
		// TODO Auto-generated method stub
		return this.problem.getGoalState();
	}
}
