package letters;

import java.util.List;

import model.algorithms.Action;
import model.algorithms.Distance;
import model.algorithms.State;
import model.algorithms.a_star.Astar;
import model.algorithms.bfs.BFS;

public class LettersTestRun {
	public static void main(String[] args)
	{
		Letter letters=new Letter("JAVA","VAJA");//Takes time to run this when adding more chars..
		BFS<String> as=new Astar<String> 
		( 
				new LetterDomain( letters)
				,new Distance<String>() {

					@Override
					public double getDistance(State<String> from,State<String> to) 
					{
						return to.getG() - from.getG();
					}
				}
				,new Distance<String>() {

					@Override
					public double getDistance(State<String> from,State<String> to) 
					{
						String fromStr = from.getObj();
						String toStr = to.getObj();
						double retVal = 0;
						for(int i=0;i<fromStr.length();i++)
						{
							if(toStr.charAt(i)!=fromStr.charAt(i))
								retVal++;
						}
						return Math.pow(retVal, toStr.length());//this would make the difference between each swap.
					}
				}	
		);
		List<Action<String>> actions = as.search();
		for (Action<String> a : actions)
			System.out.println(a.getName());
		System.out.println(as.getNumOfEvaluatedNodes());
	}
}
