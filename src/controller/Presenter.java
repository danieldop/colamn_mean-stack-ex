package controller;

import java.util.Observable;
import java.util.Observer;

import consts.CommandsConsts;
import model.Model2048;
import model.Model;
import view.View;
import view.View2048;

public class Presenter implements Observer
{
	private Model m;
	private View v;
	public static void main(String[] args)
	{
		new Presenter(new Model2048(),new View2048());
	}
	public Presenter(Model m,View v)
	{
		this.setV(v);
		this.setM(m);
		
		Thread viewThread = new Thread(v);
		viewThread.start();
	}
	@Override
	public void update(Observable arg0, Object arg1) 
	{
		int command = this.v.getUserCommand();
		if(command<CommandsConsts.MAX_ACTION_COMMAND)
		{
			if(this.m.doAction(command))
				this.refresh();	
		}
	}
	public void setV(View v) 
	{
		this.v = v;
		v.addObserver(this);
	}
	public void setM(Model m)
	{
		this.m = m;
		m.addObserver(this);
	}
	private void refresh()
	{
		this.v.displayData(this.m.getData());
		//this.v.displayScore(this.m.getScore());
	}
}
