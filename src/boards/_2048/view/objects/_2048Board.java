package boards._2048.view.objects;


import mvp.interfaces.view.objects.Board;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;

public class _2048Board extends Board {
	private final int padding = 5;

	public _2048Board(Composite parent, int style) {
		super(parent, style);
		this.setBackground(new Color(getDisplay(), 187, 173, 160));
	}

	@Override
	public void setData(int i, int j) {
		int elem = boardData[i][j];
		String text = String.valueOf(elem);

		int x = ((getBounds().width - padding * 2) / boardData.length);
		int y = ((getBounds().height - padding * 2) / boardData[0].length);

		switch (elem) {
		case 0:
			gc.setBackground(new Color(getDisplay(), 204, 192, 179));
			break;
		case 2:
			gc.setForeground(new Color(getDisplay(), 0, 0, 0));
			gc.setBackground(new Color(getDisplay(), 238, 228, 218));
			break;
		case 4:
			gc.setForeground(new Color(getDisplay(), 0, 0, 0));
			gc.setBackground(new Color(getDisplay(), 237, 224, 200));
			break;
		case 8:
			gc.setForeground(new Color(getDisplay(), 255, 255, 255));
			gc.setBackground(new Color(getDisplay(), 242, 177, 121));
			break;
		case 16:
			gc.setForeground(new Color(getDisplay(), 255, 255, 255));
			gc.setBackground(new Color(getDisplay(), 245, 149, 99));
			break;
		case 32:
			gc.setForeground(new Color(getDisplay(), 255, 255, 255));
			gc.setBackground(new Color(getDisplay(), 246, 124, 95));
			break;
		case 64:
			gc.setForeground(new Color(getDisplay(), 255, 255, 255));
			gc.setBackground(new Color(getDisplay(), 246, 94, 59));
			break;
		case 128:
			gc.setForeground(new Color(getDisplay(), 255, 255, 255));
			gc.setBackground(new Color(getDisplay(), 237, 206, 113));
			break;
		case 256:
			gc.setForeground(new Color(getDisplay(), 255, 255, 255));
			gc.setBackground(new Color(getDisplay(), 237, 204, 99));
			break;
		case 512:
			gc.setForeground(new Color(getDisplay(), 255, 255, 255));
			gc.setBackground(new Color(getDisplay(), 236, 200, 80));
			break;
		case 1024:
			gc.setForeground(new Color(getDisplay(), 255, 255, 255));
			gc.setBackground(new Color(getDisplay(), 237, 197, 63));
			break;
		case 2048:
			gc.setForeground(new Color(getDisplay(), 255, 255, 255));
			gc.setBackground(new Color(getDisplay(), 238, 194, 46));
			break;
		default:
			gc.setForeground(new Color(getDisplay(), 255, 255, 255));
			gc.setBackground(new Color(getDisplay(), 237, 204, 99));
			break;
		}

		gc.fillRectangle(x * j + padding, y * i + padding, x - padding, y
				- padding);
		if (elem > 1) {
			Font f = new Font(null, "Helvetica", x / 3, 1);
			gc.setFont(f);
			gc.drawText(text, x / 2 + j * x - (text.length() * ((x / 3) / 3)),
					(y - x / 3 - 5) / 2 + i * y);
			f.dispose();
		}

	}

}
