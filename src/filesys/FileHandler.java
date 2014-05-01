package filesys;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class FileHandler {
	/**
	 * 
	 * @param o
	 * @param path
	 * @throws IOException
	 * 
	 * Saves the object as xml file.(using xStream)
	 * all objects and sub-objects this object may hold, must have default constructors for them to be loaded from the file.
	 */
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
	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * 
	 * Load an xml file.
	 * all objects and sub-objects this object may hold, must have default constructors for them to be loaded from the file.
	 */
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
