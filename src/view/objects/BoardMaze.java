package view.objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class BoardMaze extends Board 
{
	Label mouse = new Label( this, SWT.BORDER );
	
	final static ImageData MOUSE = new ImageData("resources/images/MOUSE.png");
	final static ImageData WALL = new ImageData("resources/images/WALL.jpg");
	final static ImageData CHEESE =  new ImageData("resources/images/CHEESE.png");
	final static ImageData CHEESE_MOUSE =  new ImageData("resources/images/MOUSE&CHEESE.png");
	
	public BoardMaze(Composite parent, int style) 
	{
		super(parent, style);
		setMouseDrag();
	}
	
	private void setMouseDrag() 
	{
		DragSource dt = new DragSource(mouse,DND.DROP_MOVE | DND.DROP_COPY);
		dt.setTransfer(new Transfer[]{TextTransfer.getInstance()});
		dt.addDragListener(new DragSourceListener() {
			
			@Override
			public void dragStart(DragSourceEvent e)
			{
			}
			
			@Override
			public void dragSetData(DragSourceEvent e) 
			{
				e.data = "MOVE IT MOUSE!";
			}
			
			@Override
			public void dragFinished(DragSourceEvent arg0) 
			{
				
			}
		});
	}

	private void setMouseLabel(int i,int j,int x,int y)
	{
		if(mouse.getImage()!=null)
			mouse.getImage().dispose();
		
		Image myImage = new Image( getDisplay(), MOUSE.scaledTo(x, y));
		mouse.setImage(myImage);
		mouse.setBounds(j*x,i*y,x,y);
		mouse.setBackground(new Color(getDisplay(),225,150,186));
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
			setMouseLabel(i,j,x,y);
			return;
		case 2:
			imgData = CHEESE;
		break;
		case 3:
			imgData = new ImageData("resources/images/MOUSE&CHEESE.png");
			if(mouse.getImage()!=null) {mouse.getImage().dispose();mouse.setBackground(getBackground());mouse.setImage(null);}//NO LONGER EXISTS..
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
