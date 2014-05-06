package boards._2048.model.objects;


public class _2048State {

	private int[][] boardState;
	private int score;
	public _2048State()//for load object.
	{
		
	}
	public _2048State(int[][] boardState,int score)
	{
		this.boardState = boardState;
		this.score = score;
	}
	public int[][] getBoardState() {
		return this.boardState;
	}
	public int getScore() 
	{
		return score;
	}
}
