/**
 * 
 */
package letters;

import model.algorithms.Action;
import model.algorithms.State;

/**
 * @author omerpr
 */
public class LettersAction implements Action<String>{
/*
 * Explanation:
 * I wanted Action classes to be able to make the action for any state they get.
 * there for the action will not be just a string of the action to do(such as VAJA OR JAVA)
 * 
 * ----	but to represent the indexes the action is suppost to act on!!!
 * 		and thats how i see this classes(the classes implementing Action), 
 * 		suppose to make the action on a string, not just to return new state.----
 */
	
	
	private int[] action;
	public LettersAction(int[] action)
	{
		this.action = action;
	}
	
	@Override
	public State<String> doAction(String s) 
	{
		String newStateStr = makeSwap(s);
		return new State<String>(newStateStr,newStateStr);
	}

	private String makeSwap(String s)
	{
		return s.substring(0,this.action[0])+s.charAt(this.action[1])+s.substring(this.action[0]+1,this.action[1])+s.charAt(this.action[0])+s.substring(this.action[1]+1);
	}
	
	@Override
	public String getName() 
	{
		return "Swap char number "+String.valueOf(action[0]+1)+" and char number "+String.valueOf(action[1]+1);
	}

}
