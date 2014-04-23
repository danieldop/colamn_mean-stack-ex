package view;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;

public class Board2048 extends Board {

	public Board2048(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void setData(int i, int j) {
		int elem = boardData[i][j];
		String text = String.valueOf(elem);

		int x = ((getBounds().width) / boardData.length);
		int y = ((getBounds().height) / boardData[0].length);
	
		
		 switch(elem)
		 {
		 case 0:		
				break;
		 default:
			 gc.drawText(text,x/2-5-(text.length())+j*x,y/2+i*y-5);
		 break;
		 }
		// if(imgData!=null)
		// {
		// imgData = imgData.scaledTo(x, y);
		// Image img = null;
		// try
		// {
		// img = new Image(getDisplay(), imgData);
		// gc.drawImage(img,j*x, i*y);
		// }
		// finally
		// {
		// if(img!=null)
		// img.dispose();
		// }
		// }
	}

}
