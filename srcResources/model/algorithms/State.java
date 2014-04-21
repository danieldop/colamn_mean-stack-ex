package model.algorithms;




public class State<T>
{
	
	private double g,f;
	private String stateStr;
	private State<T> prevState;
	private T obj;
	private Action<T> prevAction;
	public State()//FOR XML ENCODER.
	{
		
	}
	public State(String stateStr,T obj)
	{
		this.stateStr = stateStr;
		this.obj = obj;
	}
	public double getF() {
		return f;
	}
	public void setF(double f) {
		this.f = f;
	}
	public double getG() {
		return g;
	}
	public void setG(double g) {
		this.g = g;
	}
	public State<T> getPrevState() {
		return prevState;
	}
	public void setPrevState(State<T> prevState) {
		this.prevState = prevState;
	}
	public void setPrevAction(Action<T> prevAction)
	{
		this.prevAction = prevAction;
	}
	public Action<T> getPrevAction()
	{
		return prevAction;
	}
	@Override
	public String toString() {
		return this.stateStr;
	}
	@Override
	public boolean equals(Object o)
	{
		return o.toString().equals(this.toString());
	}
	public T getObj()
	{
		return this.obj;
	}
}
