package view.objects;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public abstract class Board extends Canvas {
	int[][] boardData; // the data of the board
	GC gc;
	public Board(Composite parent, int style) 
	{
		
		super(parent, style); // call canvas Ctor
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) 
			{
				if(boardData!=null)
				{	
					gc = e.gc;
					int j;
					for(int i=0;i<boardData.length;i++)
					{
						for(j=0;j<boardData[i].length;j++)
						{
							gc.setForeground(new Color(getDisplay(),108,108,108));
							setData(i, j);
						}
					}
				}
			}
		});
	}
	/**
	 * 
	 * @param boardData
	 * sets the board data.
	 * also calls "redraw" method of the canvas.
	 */
	public void setBoardData(int[][] boardData)
	{
		this.boardData = boardData;
		this.redraw();
	}
	public int[][] getBoardData()
	{
		return this.boardData;
	}
	/**
	 * 
	 * @param i row.
	 * @param j column. <br>
	 * an element on the board according to supplying the indexes so 
	 * the sub- Class will know the location of the specified element in the board. 
	 */
	public abstract void setData(int i,int j);
}