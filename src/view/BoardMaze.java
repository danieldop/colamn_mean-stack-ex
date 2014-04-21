package view;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;

public class BoardMaze extends Board 
{

	public BoardMaze(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void setRectangleData(int i, int j,GC gc,int width,int height) {
		int elem = boardData[i][j];
		ImageData imgData = null;
		switch(elem)
		{
		case -1:
			imgData = new ImageData("resources/images/WALL.jpg");
		break;
		case 1:
			imgData = new ImageData("resources/images/MOUSE.png");
		break;
		case 2:
			imgData = new ImageData("resources/images/CHEESE.png");
		break;
		case 3:
			imgData = new ImageData("resources/images/MOUSE&CHEESE.png");
		break;
		}
		if(imgData!=null)
		{
			imgData = imgData.scaledTo(width, height);
			Image img = null;
			try
			{
				img = new Image(getDisplay(), imgData);
				gc.drawImage(img,j*width-3, i*height-3);
			}
			finally
			{
				if(img!=null)
					img.dispose();
			}
		}
	}
	

}
