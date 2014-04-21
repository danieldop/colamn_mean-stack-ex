package helper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import model.ModelResource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
public class TesterHelper {
	public static void main(String[] args) throws Exception
	{
		/*ModelResource m = new ModelResource();
		m.setDomain("big_maze");
		m.setHeuristics("maze_standard_weights".split(","));
		m.setSearcher("bfs");
		XStream xstream = new XStream(new DomDriver());
		xstream.toXML(m, new FileWriter(new File("C:/Users/omerpr/Game.xml")));
		ModelResource mod = (ModelResource)xstream.fromXML(new FileReader(new File("C:/Users/omerpr/Game.xml")));
		System.out.println(mod.solveDomain());*/
		if(TesterHelper.class.getClass().getResource("/resources/images/MOUSE.png")!=null)
			System.out.println("NOT NULL!!!");
		
	}
}
