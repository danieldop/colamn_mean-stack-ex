package view;

import java.util.Observable;

import model.MazeModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GC;
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
	
	private void initComponents()
	{
		display = new Display();
		shell = new Shell(display);
		
		shell.setLayout(new GridLayout(7, false));
		shell.setSize(500, 500);
		shell.setMinimumSize(500,500);
		shell.setText("Choose Maze");
		
		initDataSection();
		initBoard();
		initContinueButton();
		shell.open();
	}

	private void initContinueButton() 
	{
		continueBtn = new Button(shell,SWT.PUSH);
		continueBtn.setEnabled(false);
		continueBtn.setText("Continue");
		continueBtn.setLayoutData(new GridData(SWT.LEFT,SWT.LEFT, false, false, 4,0));
		continueBtn.addMouseListener(new MouseListener() 
		{
			Integer rowsNum;
			Integer colsNum;
			boolean outOfProportion;
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				if(!outOfProportion)
				{
					Presenter.present(new MazeModel(rowsNum,colsNum,Integer.valueOf(levels.getText())),new MazeView());
					shell.dispose();
				}		
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) 
			{
				continueBtn.setEnabled(false);
				rowsNum = Integer.valueOf(rows.getText());
				colsNum = Integer.valueOf(cols.getText());
				
				outOfProportion = false;
				
				while((double)rowsNum/colsNum<(double)17/30)
				{
					rowsNum++;
					outOfProportion = true;
				}
				while((double)rowsNum/colsNum>(double)30/17)
				{
					colsNum++;
					outOfProportion = true;
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

	@Override
	public int getUserCommand() 
	{
		return this.userCommand;
	}
	private void setUserCommand(int userCommand)
	{
		this.userCommand = userCommand;
	}
	/*private void execCommand(int command) 
	{
		execCommand(command,null);
	}*/
	
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
		rows = new Text(shell,SWT.BORDER);
		gc = new GC(rows);
		
		GridData g = new GridData(SWT.FILL,SWT.TOP, false, false, 1,1);
		g.widthHint = gc.getFontMetrics().getAverageCharWidth()*2;
		
		rows.setLayoutData(g);
		rows.setTextLimit(2);
		
		Label x = new Label(shell,SWT.NONE);
		x.setText("x");
		x.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false, 1,1));
		
		cols = new Text(shell,SWT.BORDER);
		cols.setLayoutData(g);
		cols.setTextLimit(2);
		
		KeyListener k = this.getKeyListener();
		rows.addKeyListener(k);
		cols.addKeyListener(k);
		
		rows.setSelection(rows.getText().length());
		
		
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
	
	private KeyListener getKeyListener()
	{
		return new KeyListener() 
		{
			private String beforeChange="";
			private Text source;
			private boolean isNumeric(String str)
			{
			  return str.matches("^\\d+$");
			}
			@Override
			public void keyReleased(KeyEvent arg0) 
			{
				String colsStr = cols.getText();
				String rowsStr = rows.getText();
				
				if(colsStr.length()*rowsStr.length() == 0)
				{
					colsStr = rowsStr = "0";
				}
				
				if(!isNumeric(colsStr+rowsStr))
					this.source.setText(this.beforeChange);
				
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
				continueBtn.setEnabled(false);
				this.source = (Text)arg0.getSource();
				this.beforeChange = source.getText();
			}
		};
	}

}
