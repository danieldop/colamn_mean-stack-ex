package model.helper;

import java.util.Map;

import model.algorithms.Distance;
import model.algorithms.Domain;
import model.algorithms.Searcher;

public interface SearcherGenerator<T extends Searcher<?>>
{
	public T generate(Domain<?> d,Map<String,Distance<?>> distances);
}
