package model.algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class SearcherOpenList<T> 
{
	private PriorityQueue<State<T>> elemsPq;
	private HashMap<String,State<T>> elemsMap;
	public SearcherOpenList(Comparator<State<T>> compareBy)
	{
		this.elemsPq = new PriorityQueue<State<T>>(1,compareBy);
		this.elemsMap = new HashMap<String,State<T>>();
	}
	public State<T> poll()
	{
		State<T> s = elemsPq.poll();
		this.elemsMap.remove(s.toString());
		return s;
	}
	public void remove(State<T> s) 
	{
		this.elemsPq.remove(s);
		this.elemsMap.remove(s.toString());
	}
	public State<T> get(String stateStr)//the reason i made this class. get in O(1)
	{
		return this.elemsMap.get(stateStr);
	}
	public boolean add(State<T> s)
	{
		String stateStr = s.toString();
		if(this.get(stateStr)!=null)//if exists remove it.
			this.remove(s);
		this.elemsMap.put(stateStr, s);
		return this.elemsPq.add(s);
	}
	public boolean isEmpty()
	{
		return this.elemsPq.isEmpty();
	}
	public int size()
	{
		return this.elemsPq.size();
	}
}
