package boards._2048.view;

import java.util.Observable;

import mvp.consts.CommandsConsts;
import mvp.controller.Presenter;
import mvp.interfaces.view.View;
import mvp.interfaces.view.objects.Board;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import boards._2048.model._2048Model;
import boards._2048.view.objects._2048Board;

public class _2048PreView extends Observable implements View {
	private Display display;
	private Shell shell;
	private Board board;
	private Text size;
	private int userCommand = -1;
	private Button continueBtn;

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

	private void initComponents() {
		display = new Display();
		shell = new Shell(display);

		shell.setLayout(new GridLayout(4, false));
		shell.setSize(500, 500);
		shell.setLocation(display.getClientArea().width / 2 - 250,
				display.getClientArea().height / 2 - 250);
		shell.setMinimumSize(500, 500);
		shell.setText("Set 2048 Settings");
		Image icon = null;
		try
		{
			icon = new Image(display,new ImageData(getClass().getResourceAsStream("/images/2048.jpg")));
			shell.setImage(icon);
		}
		finally
		{
			if(icon!= null)
				icon.dispose();
		}
		initDataSection();
		initBoard();
		initContinueButton();

		shell.open();
	}

	private void initContinueButton() {
		continueBtn = new Button(shell, SWT.PUSH);
		// continueBtn.setEnabled(false);
		continueBtn.setText("Continue");
		continueBtn.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false,
				false, 2, 0));
		continueBtn.addMouseListener(new MouseListener() {
			Integer sizeNum;

			boolean invalidCall;

			@Override
			public void mouseUp(MouseEvent arg0) {
				if (!invalidCall) {
					Presenter.present(new _2048Model(sizeNum), new _2048View());
					shell.dispose();
				}
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				invalidCall = false;
				continueBtn.setEnabled(false);
				if (!isNumeric(size.getText())) {
					invalidCall = true;
					continueBtn.setEnabled(true);
					return;
				}
				sizeNum = Integer.valueOf(size.getText());

				size.setText(String.valueOf(sizeNum));

				Integer dataHelper = sizeNum;
				execCommand(CommandsConsts.VIEW_TEC_BOARD_PREVIEW, dataHelper);
				continueBtn.setEnabled(true);
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
			}
		});
	}

	private boolean isNumeric(String str) {
		return str.matches("^\\d+$");
	}

	private void execCommand(int command, Object data) {
		this.setUserCommand(command);
		setChanged();
		notifyObservers(data);
	}

	private void setUserCommand(int userCommand) {
		this.userCommand = userCommand;
	}

	@Override
	public int getUserCommand() {
		return this.userCommand;
	}

	@Override
	public void addObserver(Presenter p) {
		super.addObserver(p);
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

	private void initDataSection() {
		GC gc = null;

		try {
			Label x = new Label(shell, SWT.NONE);
			x.setText("Select Matrix Size");
			x.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

			size = new Text(shell, SWT.BORDER);
			gc = new GC(size);

			GridData g = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
			g.widthHint = gc.getFontMetrics().getAverageCharWidth() * 3;

			size.setLayoutData(g);
			size.setTextLimit(2);

			size.addKeyListener(this.getDynamicBoardKeyListener());
			size.setSelection(size.getText().length());

		} finally {
			if (gc != null)
				gc.dispose();
		}
	}

	private void initBoard() {
		Board board = new _2048Board(shell, SWT.BORDER);
		board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 50));
		this.board = board;
	}

	private KeyListener getDynamicBoardKeyListener() {
		return new KeyListener() {
			private Text source;

			@Override
			public void keyReleased(KeyEvent arg0) {

				String sizeStr = size.getText();

				if (!isNumeric(sizeStr)) {

					sizeStr = "";
				}

				if (sizeStr.length() * sizeStr.length() == 0) {
					sizeStr = "0";
				}

				Integer sizeNum = Integer.valueOf(sizeStr);

				Integer dataHelper;
				if (sizeNum == 0) {
					dataHelper = 0;
				} else {
					// if(rowsNum>GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS)
					// rowsNum = GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS;
					// if(colsNum>GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS)
					// colsNum = GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS;

					size.setText(String.valueOf(sizeNum));

					continueBtn.setEnabled(true);
				}
				this.source.setSelection(this.source.getText().length());
				 dataHelper = sizeNum;
				execCommand(CommandsConsts.VIEW_TEC_BOARD_PREVIEW, dataHelper);
				continueBtn.setEnabled(true);

			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				this.source = (Text) arg0.getSource();
			}
		};
	}

	
	
	@Override
	public void displayScore(String score) {
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				// score.setText(score);
			}
		});

	}

	@Override
	public void showWonGame() {
		return;
		// do nothing. game hasn't started yet.

	}

	@Override
	public void showLostGame() {
		// TODO Auto-generated method stub
		return;
	}

}
