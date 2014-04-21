package model.algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbsSearcher<T> implements Searcher<T>{

	private SearcherOpenList<T> openList;
	private Map<String,State<T>> closedMap;
	private Comparator<State<T>> comp;
	private int evaluatedNodesNum;
	
	protected AbsSearcher(Comparator<State<T>> comp)//You use this class? you need to compare :>
	{
		this.comp = comp;
	}
	
	private void initSearcher()
	{
		this.closedMap = new HashMap<String, State<T>>();
		this.openList = new SearcherOpenList<T>(this.comp);
		this.evaluatedNodesNum = 0;
	}
	
	@Override
	public List<Action<T>> search()
	{
		this.initSearcher();
		return this.doSearch();
	}
	protected abstract List<Action<T>> doSearch();//decided to make this abstract and the search method to initiate all params and then call this method.

	@Override
	public int getNumOfEvaluatedNodes() {
		return evaluatedNodesNum;
	}
	
	protected void addToOpenList(State<T> s)
	{
		this.openList.add(s);
	}
	protected void putInClosedMap(State<T> s)
	{
		this.closedMap.put(s.toString(),s);
	}
	
	protected State<T> getFromClosedMap(String stateStr)
	{
		return this.closedMap.get(stateStr);
	}
	protected State<T> getFromOpenList(String stateStr)
	{
		return this.openList.get(stateStr);
	}
	
	protected boolean isOpenListEmpty()
	{
		return this.openList.isEmpty();
	}
	protected State<T> pollFromOpenList()
	{
		this.evaluatedNodesNum++;
		/*State<T> ret = this.openList.poll();
		System.out.println(ret.toString());
		return ret;*/
		return this.openList.poll();
	}
	
}
