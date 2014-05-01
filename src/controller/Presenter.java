package controller;

import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;
import consts.CommandsConsts;

public class Presenter implements Observer
{
	private Model m;
	private View v;
	/**
	 * 
	 * @param m - must supply model.
	 * @param v - must supply view.
	 * opens a new window to run(present) your application.
	 */
	public static void present(Model m,View v)//Open a window.
	{
		new Presenter(m,v);
	}
	public Presenter(Model m,View v)//setting the model and view, starting the view in a new thread.
	{
		this.setV(v);
		this.setM(m);
		
		Thread viewThread = new Thread(v);
		viewThread.start();
	}

	@Override
	public void update(Observable arg0, Object dataHelper)//when model or view are updated presenter decides where to dispatch the call.
	{
		int command = this.v.getUserCommand();//so far only view can send commands.
		if(command<CommandsConsts.VIEW_MOVEMENT_MAX_COMMAND)
		{
			if(this.m.doMovement(command))
				this.refreshView();	
		}
		else if(command<CommandsConsts.VIEW_TEC_MAX_COMMAND)
		{
			if(this.m.doTecAction(command, dataHelper))
				this.refreshView();
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
	private void refreshView()
	{
		this.v.displayData(this.m.getData());
		this.v.displayScore(String.valueOf(this.m.getScore()));
	}
}
