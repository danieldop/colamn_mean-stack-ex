package model.algorithms;


public abstract class Problem<T>
{
	State<T> startState; 
	State<T> goalState;
	public Problem(State<T> start,State<T> goal)
	{
		this.startState = start;
		this.goalState = goal;
	}
	public void setGoalState(State<T> goalState)
	{
		this.goalState = goalState;
	}
	public void setStartState(State<T> startState)
	{
		this.startState = startState;
	}
	public State<T> getStartState()
	{
		return this.startState;
	}
	public State<T> getGoalState()
	{
		return this.goalState;
	}
}
