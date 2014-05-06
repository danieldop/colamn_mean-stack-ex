package menu.main.model;

import mvp.controller.Presenter;
import mvp.interfaces.model.Model;

public class MenuModel implements Model {

	@Override
	public boolean doMovement(int userCommand) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doTecAction(int userCommand, Object dataHelper) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[][] getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addObserver(Presenter presenter) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getModelCommand() {
		// TODO Auto-generated method stub
		return 0;
	}

}
