package mvp.controller;

import java.util.Observable;
import java.util.Observer;

import mvp.consts.CommandsConsts;
import mvp.interfaces.model.Model;
import mvp.interfaces.view.View;

public class Presenter implements Observer {
	private Model m;
	private View v;

	/**
	 * 
	 * @param m
	 *            - must supply model.
	 * @param v
	 *            - must supply view. opens a new window to run(present) your
	 *            application.
	 */
	public static void present(Model m, View v)// Open a window.
	{
		new Presenter(m, v);
	}

	public Presenter(Model m, View v)// setting the model and view, starting the
										// view in a new thread.
	{
		this.setV(v);
		this.setM(m);

		Thread viewThread = new Thread(v);
		viewThread.start();
	}

	@Override
	public void update(Observable arg0, Object dataHelper)// when model or view
															// are updated
															// presenter decides
															// where to dispatch
															// the call.
	{
		if (arg0.equals(this.v))// view call.
		{
			handleViewCall(this.v.getUserCommand(), dataHelper);
			return;
		} else// model call.
		{
			handleModelCall(this.m.getModelCommand(), dataHelper);
		}
	}

	private void handleViewCall(int command, Object dataHelper) {
		if (command < CommandsConsts.VIEW_MOVEMENT_MAX_COMMAND) {
			if (this.m.doMovement(command))
				this.refreshView();
		} else if (command < CommandsConsts.VIEW_TEC_MAX_COMMAND) {
			if (this.m.doTecAction(command, dataHelper))
				this.refreshView();
		}
	}

	private void handleModelCall(int command, Object dataHelper) {
		switch (command) {
		case CommandsConsts.MODEL_SHOW_GENERATED_MAZE:
			this.refreshView();
			break;
		case CommandsConsts.MODEL_SHOW_WON_GAME:
			this.v.showWonGame();
			break;
		case CommandsConsts.MODEL_SHOW_LOST_GAME:
			this.v.showLostGame();
			break;
		}
	}

	public void setV(View v) {
		this.v = v;
		v.addObserver(this);
	}

	public void setM(Model m) {
		this.m = m;
		m.addObserver(this);
	}

	private void refreshView() {
		this.v.displayData(this.m.getData());
		this.v.displayScore(String.valueOf(this.m.getScore()));
	}
}
