package model.algorithms.bfs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import model.algorithms.AbsSearcher;
import model.algorithms.Action;
import model.algorithms.Distance;
import model.algorithms.Domain;
import model.algorithms.State;


public class BFS<T> extends AbsSearcher<T>{

	protected Domain<T> domain;
	protected Distance<T> g;
	public BFS()//FOR XML CONVERSIONS.
	{
		super(null);
	}
	public BFS(Domain<T> domain,Distance<T> g,Comparator<State<T>> comp)//giving the option for my own comparator :)
	{
		super(comp);
		this.setDomain(domain);
		this.setGDistance(g);
	}
	public BFS(Domain<T> domain, Distance<T> g)//With default comparator
	{
		super(new Comparator<State<T>>(){

			@Override
			public int compare(State<T> o1, State<T> o2) {
				double res = o1.getF()-o2.getF();
				return res==0 ?0 :res>0 ?1 :-1;
			}});
		this.setDomain(domain);
		this.setGDistance(g);
	}
	@Override
	protected List<Action<T>> doSearch()
	{	
		State<T> start = this.domain.getStartState(),goal = this.domain.getGoalState();
		
		initSearch(start,goal);
		State<T> q,qTag,qTemp;
		double tentativeG;
		String qTagStateStr;
		
		while(!this.isOpenListEmpty())
		{
			q = this.pollFromOpenList();
			if(q.equals(goal))
			{
				return reconstructActions(start,q);
			}
			this.putInClosedMap(q);//9
			for(Action<T> a:this.domain.getActions(q))
			{
				qTag = a.doAction(q.getObj());
				qTagStateStr = qTag.toString();
				
				tentativeG = this.g.getDistance(start, q)+this.domain.neighborsDifference(q, qTag);
				qTemp = this.getFromClosedMap(qTagStateStr);
				
				if(qTemp!=null && tentativeG>=qTemp.getG()) 
					continue;//13
				
				qTemp = this.getFromOpenList(qTagStateStr);
				if(qTemp==null || tentativeG<qTemp.getG())
				{
					qTag.setPrevState(q);
					qTag.setPrevAction(a);
					
					qTag.setG(tentativeG);
					this.setFScore(qTag);
					this.addToOpenList(qTag);
				}
			}
		}
		
		return null;//never got to the goal..?
	}
	protected void setFScore(State<T> s)
	{
		s.setF(this.g.getDistance(this.domain.getStartState(),s));
	}
	private ArrayList<Action<T>> reconstructActions(State<T> start,State<T> current) 
	{
		/*
		 * Using Stack so adding to results will take O(1) instead of O(n)
		 */
		ArrayList<Action<T>> results = new ArrayList<Action<T>>();
		Stack<Action<T>> helper = new Stack<Action<T>>(); 
		while(!current.equals(start))
		{
			helper.push(current.getPrevAction());
			current = current.getPrevState();
		}
		while(!helper.isEmpty())
		{
			results.add(helper.pop());
		}
		return results;
	}
	private void initSearch(State<T> start,State<T> goal)
	{
		start.setG(0);
		this.setFScore(start);
		this.addToOpenList(start);
	}
	  
	public void setDomain(Domain<T> domain) 
	{
		this.domain = domain;
	}
	public void resetBFS(Domain<T> domain,Distance<T> g)
	{
		this.setDomain(domain);
		this.setGDistance(g);
	}
	public Domain<T> getDomain() 
	{
		return domain;
	}
	public Distance<T> getGDistance() {
		return g;
	}
	public void setGDistance(Distance<T> g) {
		this.g = g;
	}
}
 