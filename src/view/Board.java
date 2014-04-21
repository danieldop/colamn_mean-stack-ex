package view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public abstract class Board extends Canvas {
	int[][] boardData; // the data of the board
	final Canvas c;
	public Board(Composite parent, int style) 
	{
		
		super(parent, style); // call canvas Ctor
		setBackground(new Color(getDisplay(),255,255,255));//White.
		// add the paint listener
		c = this;
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) 
			{
				if(boardData!=null)
				{
					//System.out.println(c.getBounds().width);
					//System.out.println(c.getBounds().height);
					int x = (c.getBounds().width/boardData.length);
					int y = (c.getBounds().height/boardData[0].length);
					//Rectangle rect = getClientArea();
					//rect.
					GC gc = new GC(c); 
					//gc.fillRectangle(rect);
					int j;
					for(int i=0;i<boardData.length;i++)
					{
						for(j=0;j<boardData[i].length;j++)
						{
							gc.fillRectangle(x*j,y*i,y,x);
							setRectangleData(i, j,gc,x,y);
						}					
					}
				}
			}
		});
	}
	public void setBoardData(int[][] boardData)
	{
		this.boardData = boardData;
		this.redraw();
	}
	public abstract void setRectangleData(int i,int j,GC gc,int width,int height);
}