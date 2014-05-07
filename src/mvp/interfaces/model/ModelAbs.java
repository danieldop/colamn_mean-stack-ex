package mvp.interfaces.model;

import java.util.Observable;

import mvp.controller.Presenter;

public abstract class ModelAbs extends Observable implements Model
{
	protected int[][] board;
	protected int score = 0;
	protected boolean isWin;
	protected boolean isLost;
	protected int modelCommand = -1;
	protected int rows,cols;
	
	@Override
	public int getScore()
	{
		return this.score;
	}
	
	public void addObserver(Presenter presenter) 
	{
		super.addObserver(presenter);
	};
	
	@Override
	public int[][] getData() 
	{
		return this.board;
	}
	

	
	protected void execCommand(int command)
	{
		setModelCommand(command);
		setChanged();
		notifyObservers();
	}

	private void setModelCommand(int command) {
		this.modelCommand = command;
		
	}
	
	@Override
	public int getModelCommand() {
		return this.modelCommand;
	}
	
	protected abstract boolean undoMove(); 
	protected abstract ModelAbs save();
	
}
