package model.algorithms.a_star;

import java.util.Comparator;

import model.algorithms.Distance;
import model.algorithms.Domain;
import model.algorithms.State;
import model.algorithms.bfs.BFS;


public class Astar<T> extends BFS<T>{
	
	protected Distance<T> h;
	
	public Astar()//FOR XML CONVERSIONS.
	{
		
	}
	public Astar(Domain<T> domain, Distance<T> g, Distance<T> h)
	{
		super(domain, g);
		this.setHDistance(h);
	}  
	public Astar(Domain<T> domain, Distance<T> g, Distance<T> h,Comparator<State<T>> comp)
	{
		super(domain, g,comp);
		this.setHDistance(h);
	}  
	public void resetBFS(Domain<T> domain,Distance<T> g,Distance<T> h)
	{
		super.resetBFS(domain, g);
		this.setHDistance(h);
	}
	@Override
	protected void setFScore(State<T> s)
	{
		s.setF(this.g.getDistance(this.domain.getStartState(),s)+this.h.getDistance(s,this.domain.getGoalState()));
	}
	public Distance<T> getHDistance() {
		return h;
	}
	public void setHDistance(Distance<T> h) {
		this.h = h;
	}
}
 