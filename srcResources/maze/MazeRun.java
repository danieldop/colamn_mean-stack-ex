package maze;

import java.util.List;

import model.algorithms.Action;
import model.algorithms.Distance;
import model.algorithms.State;
import model.algorithms.a_star.Astar;
import model.algorithms.bfs.BFS;

public class MazeRun 
{
	public static void main(String[] args)
	{
		Maze maze= new Maze(19,19,new int[][]{{1,0},{1,1},{1,2},{1,3}},new int[]{17,10},new int[]{0,0}); 
		BFS<int[]> as =new Astar<int[]>
		( 
				new MazeDomain( maze )
				,new Distance<int[]>() 
				{//G DISTANCE CALCULATOR
	
					@Override
					public double getDistance(State<int[]> from, State<int[]> to) 
					{
						return to.getG() - from.getG();
					}
				}
				,new Distance<int[]>() 
				{//H DISTANCE CALCULATOR
	
					@Override
					public double getDistance(State<int[]> from, State<int[]> to) 
					{
						int[] fromObj = from.getObj();
						int[] toObj = to.getObj();
						double distRow = fromObj[0]-toObj[0];
						double distCol = fromObj[1]-toObj[1];
						return 10*Math.sqrt(distRow*distRow+distCol*distCol);
					}
				}
		);
		List<Action<int[]>> actions = as.search();
		for (Action<int[]> a : actions)
		{
			System.out.println(a.getName());
		}
		System.out.println(as.getNumOfEvaluatedNodes());
	}
}
