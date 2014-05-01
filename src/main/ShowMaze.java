package main;

import view.MazePreView;
import model.MazeModel;
import controller.Presenter;

public class ShowMaze 
{
	public static void main(String[] args)
	{
		//present the preview of the maze.
		Presenter.present(new MazeModel(),new MazePreView());
	}
}
