package filesys;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class FileHandler {
	public static void saveObject(Object o,String path) throws IOException
	{
		FileWriter f = new FileWriter(new File(path));
		try
		{
		XStream xstream = new XStream(new DomDriver());
		xstream.toXML(o,f);
		}
		finally
		{
			f.close();
		}
	}
	public static Object loadObject(String path) throws IOException
	{
		FileReader f = new FileReader(new File(path));
		Object retValue = null;
		try
		{
			XStream xstream = new XStream(new DomDriver());
			
			retValue =  xstream.fromXML(f);
			return retValue;
		}
		finally
		{
			f.close();
		}
	}
}
