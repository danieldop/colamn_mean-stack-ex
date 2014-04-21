package model.algorithms;

import java.util.List;

public interface Searcher<T>
{
	public List<Action<T>> search();
	public int getNumOfEvaluatedNodes();
}
