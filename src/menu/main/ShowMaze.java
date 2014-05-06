package menu.main;

import menu.main.model.MenuModel;
import menu.main.view.MenuView;
import mvp.controller.Presenter;
import boards.maze.model.MazeModel;
import boards.maze.view.MazePreView;

public class ShowMaze 
{
	public static void main(String[] args)
	{
		//present the preview of the maze.
		//Presenter.present(new MazeModel(),new MazePreView());
		Presenter.present(new MenuModel(),new MenuView());
	}
}
