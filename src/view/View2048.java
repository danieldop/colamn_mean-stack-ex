package view;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import consts.CommandsConsts;
import controller.Presenter;

public class View2048 extends Observable implements View {
	private Display display;
	private Shell shell;
	private Board board;
	private int userCommand;

	private void initComponents() {
		display = new Display();
		shell = new Shell(display);

		shell.setLayout(new GridLayout(2, false));
		shell.setSize(300, 350);
		shell.setText("my 2048 game");
		Button b1 = new Button(shell, SWT.PUSH);
		b1.setText("Undo");
		b1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		b1.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				setUserCommand(8);
				refresh();
			}

		});
		Button b2 = new Button(shell, SWT.PUSH);

		b2.setText("New Game");
		b2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

		initBoard();

		setKeyEvents();

		shell.open();
	}

	@Override
	public void run() {
		initComponents();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	@Override
	public void displayData(final int[][] data) {
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				board.setBoardData(data);
			}
		});

	}

	@Override
	public int getUserCommand() {
		return this.userCommand;
	}

	private void setUserCommand(int userCommand) {
		this.userCommand = userCommand;
	}

	private void initBoard() {
		Board board = new Board2048(shell, SWT.BORDER);
		board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
		this.board = board;

		this.setUserCommand(CommandsConsts.INITIATE);
		this.refresh();
	}

	private void refresh() {
		setChanged();
		notifyObservers();
		this.board.setFocus();
	}

	private void setKeyEvents() {
		this.board.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.keyCode) {
				case SWT.ARROW_UP:
					setUserCommand(CommandsConsts.UP);
					break;
				case SWT.ARROW_DOWN:
					setUserCommand(CommandsConsts.DOWN);
					break;
				case SWT.ARROW_LEFT:
					setUserCommand(CommandsConsts.LEFT);
					break;
				case SWT.ARROW_RIGHT:
					setUserCommand(CommandsConsts.RIGHT);
					break;
				default:// no valid command.
					return;
				}
				refresh();
			}
		});
	}

	@Override
	public void addObserver(Presenter presenter) {
		super.addObserver(presenter);
	}
}
