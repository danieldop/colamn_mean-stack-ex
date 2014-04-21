package model;

import java.util.HashMap;
import java.util.Map;

import model.algorithms.Distance;
import model.algorithms.Domain;
import model.algorithms.Searcher;

public class ModelResource
{
	
	private Domain<?> domain;

	 private Map<String,Distance<?>> distances;//<Distance Description,Distance Implementation>

	 private Searcher<?> searcher;
	 
	 public void setDomain(String key)
	 {
		 this.domain = ModelFactory.getDomainByKey(key);
	 }

	 public void setHeuristics(String[] keys)
	 {
		 this.distances = new HashMap<String,Distance<?>>();//incase we reset our model :)
		 for(int i=0;i<keys.length;i++)
		 {//description could contain G,H, or even G1 G2 H1 H2 etc..
			 this.distances.put(ModelFactory.getHeuristicDescriptionByKey(keys[i]), ModelFactory.getHeuristicImplByKey(keys[i]));
		 }
	 }

	 public void setSearcher(String key)
	 {
		 this.searcher = ModelFactory.getSearcherByKeyAndParams(key, this.domain, this.distances);
	 }

	 public int solveDomain()
	 {
		 this.searcher.search();
		 return this.searcher.getNumOfEvaluatedNodes();
	 }
}
