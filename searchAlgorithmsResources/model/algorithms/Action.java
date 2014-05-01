package model.algorithms;

public interface Action<T>
{
	public State<T> doAction(T stateObj);
	public String getName();
}
