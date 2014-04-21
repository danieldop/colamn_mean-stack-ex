package view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class Board extends Canvas {
	int[][] boardData; // the data of the board

	public Board(Composite parent, int style) {
		super(parent, style); // call canvas Ctor
		// add the paint listener
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) 
			{	
				if (boardData != null)
				{
					// paint the board...
				}
			}
		});
	}
	public void setBoardDate(int[][] boardData)
	{
		this.boardData = boardData;
	}
}