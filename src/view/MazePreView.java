package view;

import java.util.Observable;

import model.MazeModel;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import view.objects.Board;
import view.objects.BoardMaze;
import consts.CommandsConsts;
import consts.GeneralConsts;
import controller.Presenter;

public class MazePreView extends Observable implements View
{
	private Display display;
	private Shell shell;
	private Board board;
	private Text rows;
	private Text cols;
	private int userCommand = -1;
	private Button continueBtn;
	private Combo levels;
	@Override
	public void run() 
	{
		initComponents();
		
		while(!shell.isDisposed())
		{
			if(!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		display.dispose();
	}
	/**
	 * @author omerpr
	 * @
	 * initiates display,shell
	 * @
	 * initiates score,board
	 * @
	 * initiates left(data) section.
	 * @
	 * initiates the continue button.
	 * @
	 * opens shell when done.
	 */
	private void initComponents()
	{
		display = new Display();
		shell = new Shell(display);
		
		shell.setLayout(new GridLayout(7, false));
		shell.setSize(500, 500);
		shell.setLocation(display.getClientArea().width/2-250,display.getClientArea().height/2-250);
		shell.setMinimumSize(500,500);
		shell.setText("Set Maze Settings");
		
		Image icon = null;
		try
		{
			icon = new Image(display,new ImageData(getClass().getResourceAsStream("/images/MAZE.jpg")));
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

	private void initContinueButton() 
	{
		continueBtn = new Button(shell,SWT.PUSH);
		//continueBtn.setEnabled(false);
		continueBtn.setText("Continue");
		continueBtn.setLayoutData(new GridData(SWT.LEFT,SWT.LEFT, false, false, 4,0));
		continueBtn.addMouseListener(new MouseListener() 
		{
			Integer rowsNum;
			Integer colsNum;
			boolean invalidCall;
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				if(!invalidCall)
				{
					Presenter.present(new MazeModel(rowsNum,colsNum,Integer.valueOf(levels.getText())),new MazeView());
					shell.dispose();
				}		
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) 
			{
				invalidCall = false;
				continueBtn.setEnabled(false);
				if(!isNumeric(rows.getText()) || !isNumeric(cols.getText()))
				{
					invalidCall = true;
					continueBtn.setEnabled(true);
					return;
				}
				rowsNum = Integer.valueOf(rows.getText());
				colsNum = Integer.valueOf(cols.getText());
				
				if(rowsNum*colsNum == 0)
					return;
				
				if(rowsNum>GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS)
				{
					rowsNum = GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS;
					rows.setText(String.valueOf(rowsNum));
					invalidCall = true;
				}
				if(colsNum>GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS)
				{
					colsNum = GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS;
					cols.setText(String.valueOf(colsNum));
					invalidCall = true;
				}

				while((double)rowsNum/colsNum<(double)17/30)
				{
					rowsNum++;
					invalidCall = true;
				}
				while((double)rowsNum/colsNum>(double)30/17)
				{
					colsNum++;
					invalidCall = true;
				}
				rows.setText(String.valueOf(rowsNum));
				cols.setText(String.valueOf(colsNum));
				
				Integer[] dataHelper = new Integer[]{rowsNum,colsNum};
				execCommand(CommandsConsts.VIEW_TEC_BOARD_PREVIEW,dataHelper);
				continueBtn.setEnabled(true);
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
	}

	private boolean isNumeric(String str) 
	{
		return str.matches("^\\d+$");
	}

	@Override
	public int getUserCommand() 
	{
		return this.userCommand;
	}
	private void setUserCommand(int userCommand)
	{
		this.userCommand = userCommand;
	}

	private void execCommand(int command,Object data) 
	{
		this.setUserCommand(command);
		setChanged();
		notifyObservers(data);
	}
	
	@Override
	public void addObserver(Presenter p) 
	{
		super.addObserver(p);
	}

	@Override
	public void displayData(final int[][] data) 
	{
		display.syncExec(new Runnable() {
			
			@Override
			public void run() 
			{
				board.setBoardData(data);
			}
		});
		
	}

	private void initDataSection() 
	{
		GC gc = null;
		
		try
		{
			/*
			 * rows input & cols input.
			 */
			rows = new Text(shell,SWT.BORDER);
			gc = new GC(rows);
			
			GridData g = new GridData(SWT.FILL,SWT.TOP, false, false, 1,1);
			g.widthHint = gc.getFontMetrics().getAverageCharWidth()*3;
			
			rows.setLayoutData(g);
			rows.setTextLimit(2);
			
			Label x = new Label(shell,SWT.NONE);
			x.setText("x");
			x.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false, 1,1));
			
			cols = new Text(shell,SWT.BORDER);
			cols.setLayoutData(g);
			cols.setTextLimit(2);
			
			rows.addKeyListener(this.getDynamicBoardKeyListener());
			cols.addKeyListener(this.getDynamicBoardKeyListener());
			
			rows.setSelection(rows.getText().length());
			
			/*
			 * Levels label & combo box
			 */
			Label level = new Label(shell,SWT.NONE);
			level.setText("Level: ");
			level.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false, 1,1));
			
			levels = new Combo(shell, SWT.READ_ONLY);
			levels.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false, 2,1));
			levels.add(GeneralConsts.VIEW_LEVEL_ONE);
			levels.add(GeneralConsts.VIEW_LEVEL_TWO);
			levels.add(GeneralConsts.VIEW_LEVEL_THREE);
			levels.add(GeneralConsts.VIEW_LEVEL_FOUR);
			levels.add(GeneralConsts.VIEW_LEVEL_FIVE);
			levels.select(0);
		
		}
		finally
		{
			if(gc!=null)
				gc.dispose();
		}
	}
	
	private void initBoard()
	{
		Board board = new BoardMaze(shell, SWT.BORDER);
		board.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true, 1,50));
		this.board = board;
	}
	
	@Override
	public void displayScore(String score) 
	{
		//DO NOTHING. GAME HASNT STARTED YET.
	}
	
	/**
	 * when both rows and cols are set with positive numbers,
	 * the board will be build dynamically.
	 * 
	 * @return KeyListener
	 */
	private KeyListener getDynamicBoardKeyListener()
	{
		return new KeyListener() 
		{
			private Text source;
			@Override
			public void keyReleased(KeyEvent arg0) 
			{
				String colsStr = cols.getText();;
				String rowsStr = rows.getText();

				if(!isNumeric(colsStr) || !isNumeric(rowsStr))
				{
					colsStr = "";
					rowsStr = "";
				}

				
				if(colsStr.length()*rowsStr.length() == 0)
				{
					colsStr = rowsStr = "0";
				}
				

				Integer rowsNum = Integer.valueOf(rowsStr),colsNum = Integer.valueOf(colsStr);
				
				Integer[] dataHelper;
				if(rowsNum*colsNum == 0)
				{
					dataHelper = new Integer[]{};
				}
				else
				{ 
					if(rowsNum>GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS)
						rowsNum = GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS;
					if(colsNum>GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS)
						colsNum = GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS;
					
					rows.setText(String.valueOf(rowsNum));
					cols.setText(String.valueOf(colsNum));
					
					continueBtn.setEnabled(true);
				}
				this.source.setSelection(this.source.getText().length());
				dataHelper = new Integer[]{rowsNum,colsNum};
				execCommand(CommandsConsts.VIEW_TEC_BOARD_PREVIEW,dataHelper);
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) 
			{
				this.source = (Text)arg0.getSource();
			}
		};
	}

}
