package letters;

import model.algorithms.Problem;
import model.algorithms.State;


public class Letter extends Problem<String> 
{
	public Letter(String wordFrom, String wordTo) 
	{
		super(new State<String>(wordFrom,wordFrom),new State<String>(wordTo,wordTo));
	}
}
