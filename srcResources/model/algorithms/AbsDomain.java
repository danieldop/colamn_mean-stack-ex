package model.algorithms;


public abstract class AbsDomain<T,P extends Problem<T>> implements Domain<T>{
	
	protected P problem;
	public AbsDomain(P problem)
	{
		this.problem = problem;
	}
	public P getProblem()
	{
		return this.problem;
	}
	public void setProblem(P problem)
	{
		this.problem = problem;
	}
	@Override
	public State<T> getStartState()
	{
		return this.problem.getStartState();
	}
	@Override
	public State<T> getGoalState()
	{
		return this.problem.getGoalState();
	}
}
