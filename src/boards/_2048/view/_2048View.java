package boards._2048.view;

import mvp.consts.CommandsConsts;
import mvp.controller.Presenter;
import mvp.interfaces.view.ViewAbs;
import mvp.interfaces.view.objects.Board;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import boards._2048.model._2048Model;
import boards._2048.view.objects._2048Board;

public class _2048View extends ViewAbs {

	@Override
	protected Board initBoard() {
		Board board = new _2048Board(shell, SWT.BORDER);
		board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 8));

		return board;
	}

	@Override
	protected void initBoardKeyEvents() {
		this.board.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.keyCode) {
				case SWT.ARROW_UP:
					execCommand(CommandsConsts.VIEW_MOVE_UP, false);
					isGameEnded();
					break;
				case SWT.ARROW_DOWN:
					execCommand(CommandsConsts.VIEW_MOVE_DOWN, false);
					isGameEnded();
					break;
				case SWT.ARROW_LEFT:
					execCommand(CommandsConsts.VIEW_MOVE_LEFT, false);
					isGameEnded();
					break;
				case SWT.ARROW_RIGHT:
					execCommand(CommandsConsts.VIEW_MOVE_RIGHT, false);
					isGameEnded();
					break;
				default:// no valid command.
					return;
				}
			}
		});
	}

	@Override
	protected Shell initShell() {
		Shell shell = new Shell(display);

		shell.setLayout(new GridLayout(2, false));
		shell.setSize(500, 500);
		shell.setMinimumSize(500, 500);
		shell.setLocation(display.getClientArea().width / 2 - 250,
				display.getClientArea().height / 2 - 250);
		shell.setText("2048");

		return shell;
	}

	@Override
	protected Label initScore() {
		Label score = new Label(shell, SWT.BOLD);
		score.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

		return score;
	}

	@Override
	protected void initBoardMouseEvents() {

		board.addMouseListener(new MouseListener() {
			int startX, startY;
			int deltaX, deltaY;
			Cursor hand = new Cursor(display, SWT.CURSOR_SIZEALL);
			Cursor defaultCursor = new Cursor(display, SWT.CURSOR_ARROW);

			@Override
			public void mouseUp(MouseEvent e) {
				deltaX = e.x - startX;
				deltaY = e.y - startY;
				if (Math.abs(deltaX) > Math.abs(deltaY)) {
					// X is bigger
					if (deltaX > 0) {
						execCommand(CommandsConsts.VIEW_MOVE_RIGHT, false);
					} else {
						execCommand(CommandsConsts.VIEW_MOVE_LEFT, false);
					}
				}
				else
				{
					if (deltaY > 0) {
						execCommand(CommandsConsts.VIEW_MOVE_DOWN, false);
					} else {
						execCommand(CommandsConsts.VIEW_MOVE_UP, false);
						
					}
				}
				
			
				board.setCursor(defaultCursor);

			}

			@Override
			public void mouseDown(MouseEvent e) {
				startX = e.x;
				startY = e.y;
				board.setCursor(hand);
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
			}
		});

	}

	@Override
	protected String getIconPath() {
		return "/images/2048.jpg";
	}

	@Override
	protected void resetGameSettings() {

		Presenter.present(new _2048Model(4), new _2048PreView());
		try {
			Thread.sleep(50);
		} catch (Exception e) {

		}
		shell.dispose();

	}

	@Override
	public void showWonGame() {
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				openMessageBox("Winning!", "You won the game and reached 2048 tile, you can keep playing!",
						SWT.OK);
			}
		});
	}

	@Override
	public void showLostGame() {
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				openMessageBox("GameOver!", "No more steps are available",
						SWT.OK);
			}
		});
		
	}
}
