package view.objects;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class BoardMaze extends Board 
{
	
	final static ImageData MOUSE = new ImageData(Board.class.getClass().getResourceAsStream("/images/MOUSE.png"));;
	final static ImageData WALL = new ImageData(Board.class.getClass().getResourceAsStream("/images/WALL.jpg"));
	final ImageData CHEESE =  new ImageData(Board.class.getClass().getResourceAsStream("/images/CHEESE.png"));
	final ImageData CHEESE_MOUSE =  new ImageData(Board.class.getClass().getResourceAsStream("/images/MOUSE&CHEESE.png"));
	
	public BoardMaze(Composite parent, int style) 
	{
		super(parent, style);
	}

	//HERE
	
	@Override
	public void setData(int i, int j) 
	{
		int x = ((getBounds().width-5)/boardData[0].length);
		int y = ((getBounds().height-5)/boardData.length);
		
		
		
		gc.drawRectangle(x*j,y*i,x,y);
		
		int elem = boardData[i][j];
		ImageData imgData = null;
		
		switch(elem)
		{
		case -1:
			imgData = WALL;
			break;
		case 1:
			imgData = MOUSE;
			//IMPORTANT! this is used for the parent(View) to extract the location of the mouse when needed.
			setData("mousePoint",new Point(j*x,i*y));
			break;
		case 2:
			imgData = CHEESE;
			break;
		case 3:
			imgData = CHEESE_MOUSE;
			break;
		}
		if(imgData!=null)
		{
			imgData = imgData.scaledTo(x,y);
			Image img = null;
			try
			{
				img = new Image(getDisplay(), imgData);
				gc.drawImage(img,j*x,i*y);
			}
			finally
			{
				if(img!=null)
					img.dispose();
			}
		}
	}
	

}
