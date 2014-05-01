package controller.boot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import model.ModelResource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Run {

	public static void main(String[] args) throws FileNotFoundException 
	{
		//get xml file into Model Object.=>solve the domain(A* ,BFS ,etc..) print the number of moves it took.
		System.out.println(((ModelResource)(new XStream(new DomDriver()).fromXML(new FileReader(new File("resources/big_maze_Astar.xml"))))).solveDomain());
	}

}
