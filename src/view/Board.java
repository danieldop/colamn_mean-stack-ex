package view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public abstract class Board extends Canvas {
	int[][] boardData; // the data of the board
	final GC gc;
	public Board(Composite parent, int style) 
	{
		
		super(parent, style); // call canvas Ctor
		//setBackground(new Color(getDisplay(),255,255,255));//White.
		// add the paint listener
		gc = new GC(this);
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) 
			{
				if(boardData!=null)
				{
					int x = ((getBounds().width-5)/boardData.length);
					int y = ((getBounds().height-5)/boardData[0].length);
					
					int j;
					gc.setForeground(new Color(getDisplay(),108,108,108));
					for(int i=0;i<boardData.length;i++)
					{
						for(j=0;j<boardData[i].length;j++)
						{
							gc.drawRectangle(x*j,y*i,x,y);
							setData(i, j);
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
	public abstract void setData(int i,int j);
}