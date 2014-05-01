package model.algorithms;

public interface Distance<T> {

	public double getDistance(State<T> from, State<T> to); 
}
