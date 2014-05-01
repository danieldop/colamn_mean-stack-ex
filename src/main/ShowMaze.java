package main;

import boards.maze.model.MazeModel;
import boards.maze.view.MazePreView;
import controller.Presenter;

public class ShowMaze 
{
	public static void main(String[] args)
	{
		//present the preview of the maze.
		Presenter.present(new MazeModel(),new MazePreView());
	}
}
