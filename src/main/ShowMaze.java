package main;

import view.MazePreView;
import model.MazeModel;
import controller.Presenter;

public class ShowMaze 
{
	public static void main(String[] args)
	{
		Presenter.present(new MazeModel(),new MazePreView());
	}
}
