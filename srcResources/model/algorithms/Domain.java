package model.algorithms;


import java.util.List;

public interface Domain<T> 
{
	public double neighborsDifference(State<T> q,State<T> qTag);	
	public List<Action<T>> getActions(State<T> s);//valid actions to preform.
	public State<T> getStartState();
	public State<T> getGoalState();
}
