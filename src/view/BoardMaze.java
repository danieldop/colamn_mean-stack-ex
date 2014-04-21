package view;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;

public class BoardMaze extends Board 
{

	public BoardMaze(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void setData(int i, int j) 
	{
		int elem = boardData[i][j];
		ImageData imgData = null;
		int x = ((getBounds().width-5)/boardData.length);
		int y = ((getBounds().height-5)/boardData[0].length);
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
			imgData = imgData.scaledTo(x, y);
			Image img = null;
			try
			{
				img = new Image(getDisplay(), imgData);
				gc.drawImage(img,j*x, i*y);
			}
			finally
			{
				if(img!=null)
					img.dispose();
			}
		}
	}
	

}
