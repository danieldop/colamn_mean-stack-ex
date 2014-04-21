 package model;

import java.util.HashMap;
import java.util.Map;

import letters.Letter;
import letters.LetterDomain;
import maze.Maze;
import maze.MazeDomain;
import model.algorithms.Distance;
import model.algorithms.Domain;
import model.algorithms.Searcher;
import model.algorithms.State;
import model.algorithms.a_star.Astar;
import model.algorithms.bfs.BFS;
import model.helper.Generator;
import model.helper.SearcherGenerator;

public class ModelFactory {
	
	private static Map<String,Generator<Domain<?>>> domainsMap;
	private static Map<String,Distance<?>> distancesImplMap;
	private static Map<String,String> distancesDescMap;
	private static Map<String,SearcherGenerator<?>> searchersMap;
	static
	{
		domainsMap = new HashMap<String,Generator<Domain<?>>>();
		distancesImplMap = new HashMap<String,Distance<?>>();
		distancesDescMap = new HashMap<String,String>();
		searchersMap = new HashMap<String,SearcherGenerator<?>>();
		
		/*MAZE INIT - START */
		domainsMap.put("small_maze",new Generator<Domain<?>>() {
			
			@Override
			public MazeDomain generate() {
				return new MazeDomain(new Maze(3,4,new int[][]{{1,0},{1,1}},new int[]{0,0},new int[]{2,3}));
			}
		});
		domainsMap.put("big_maze",new Generator<Domain<?>>() {
			
			@Override
			public MazeDomain generate() {
				return new MazeDomain(new Maze(16,16,generateBigMazeWalls(),new int[]{0,0},new int[]{15,15}));
			}

			private int[][] generateBigMazeWalls() 
			{
				int[][] walls = new int[28][1];
				for(int i=1;i<15;i++)
				{
					walls[i-1] = new int[]{1,i};
					walls[28-i] = new int[]{i+1,1};
				}
				return walls;
			}
		});
		
		/*THIS ANNONY CLASS MAY BE LOADED TO/FROM XML, DO NOT DELETE THIS FUTURE ME.*/
		distancesImplMap.put("maze_air_distance", new Distance<int[]>() 
				{
			
			@Override
			public double getDistance(State<int[]> from, State<int[]> to) 
			{
				int[] fromObj = from.getObj();
				int[] toObj = to.getObj();
				double distRow = fromObj[0]-toObj[0];
				double distCol = fromObj[1]-toObj[1];
				return 10*Math.sqrt(Math.pow(distRow, 2)+Math.pow(distCol, 2));
			}
		});
		/*THIS ANNONY CLASS MAY BE LOADED TO/FROM XML, DO NOT DELETE THIS FUTURE ME.*/
		distancesImplMap.put("maze_standard_weights", new Distance<int[]>() 
				{
			
			@Override
			public double getDistance(State<int[]> from, State<int[]> to) 
			{
				return to.getG() - from.getG();
			}
		});
		distancesDescMap.put("maze_standard_weights","G");
		distancesDescMap.put("maze_air_distance","H");
		/*MAZE INIT - END */
		
		/*LETTERS INIT - START*/
		
		domainsMap.put("vaja_java",new Generator<Domain<?>>() {
			
			@Override
			public Domain<?> generate() {
				return new LetterDomain(new Letter("vaja","java"));
			}
		});
		
		distancesImplMap.put("letters_h", new Distance<String>() 
				{
			
			@Override
			public double getDistance(State<String> from,State<String> to) 
			{
				String fromStr = from.getObj();
				String toStr = to.getObj();
				double retVal = 0;
				for(int i=0;i<fromStr.length();i++)
				{
					if(toStr.charAt(i)!=fromStr.charAt(i))
						retVal++;
				}
				return Math.pow(retVal,toStr.length());//cause each swap is actually only one move?
			}
		});
		
		/*THIS ANNONY CLASS MAY BE LOADED TO/FROM XML, DO NOT DELETE THIS FUTURE ME.*/
		distancesImplMap.put("letters_standard_weights", new Distance<String>() 
				{
			
			@Override
			public double getDistance(State<String> from, State<String> to) 
			{
				return 10*(to.getG() - from.getG());
			}
		});
		distancesDescMap.put("letters_standard_weights","G");
		distancesDescMap.put("letters_h","H");
		/*LETTERS INIT - END*/
		
		/*SEARCHERS INIT - START */
		
		searchersMap.put("A*", new SearcherGenerator<BFS<?>>() {

			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public BFS<?> generate(Domain<?> d,Map<String,Distance<?>> distances) 
			{
				return new Astar(d,distances.get("G"),distances.get("H"));
			}
		});
		searchersMap.put("bfs", new SearcherGenerator<BFS<?>>() {

			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public BFS<?> generate(Domain<?> d,Map<String,Distance<?>> distances) 
			{
				return new BFS(d,distances.get("G"));
			}
		});
		
		/*SEARCHERS INIT - END */
	}
	
	public static Domain<?> getDomainByKey( String key)
	{
		return domainsMap.get(key).generate();
	}
	public static String getHeuristicDescriptionByKey(String key)
	{
		return distancesDescMap.get(key);
	}
	public static Distance<?> getHeuristicImplByKey(String key) 
	{
		return distancesImplMap.get(key);
	}
	public static Searcher<?> getSearcherByKeyAndParams(String key,Domain<?> domain,Map<String,Distance<?>> distances)
	{
		return searchersMap.get(key).generate(domain, distances);
	}
}
