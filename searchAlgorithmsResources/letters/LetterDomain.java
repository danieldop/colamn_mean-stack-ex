package letters;

import java.util.ArrayList;
import java.util.List;

import model.algorithms.AbsDomain;
import model.algorithms.Action;
import model.algorithms.State;

public class LetterDomain extends AbsDomain<String,Letter> {
	
	public LetterDomain()//FOR XML ENCODER :<
	{
		super(null);
	}
	
	public LetterDomain(Letter letters) {
		super(letters);
	}

	@Override
	public double neighborsDifference(State<String> q, State<String> qTag) {
		String qTagStr = qTag.getObj();
		String qStr = q.getObj();
		double retVal = 1;
		for(int i=0;i<qStr.length();i++)
		{
			if(qTagStr.charAt(i) != qStr.charAt(i))
			{
				for(int j=i+1;j<qStr.length();j++)
				{
					if(qTagStr.charAt(j)!=qStr.charAt(j))
						return retVal;
					retVal++;
				}
			}
		}
		
		return 0;
	}

	@Override
	public List<Action<String>> getActions(State<String> s) 
	{
		String stateStr = s.toString();
		List<Action<String>> results = new ArrayList<Action<String>>();
		for(int i=0;i<stateStr.length();i++)
		{
			for(int j=i+1;j<stateStr.length();j++)
			{
				if(stateStr.charAt(j)!=stateStr.charAt(i))
					results.add(new LettersAction(new int[]{i,j}));
			}
		}
		return results;
	}
}
