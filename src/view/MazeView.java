package view;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import view.objects.Board;
import view.objects.BoardMaze;
import consts.CommandsConsts;
import controller.Presenter;

public class MazeView extends Observable implements View
{
	private Display display;
	private Shell shell;
	private Board board;
	private Label score;
	private int userCommand = -1;

	private void initComponents()
	{
		display = new Display();
		shell = new Shell(display);
		
		shell.setLayout(new GridLayout(2, false));
		shell.setSize(500, 500);
		shell.setMinimumSize(500,500);
		shell.setText("Maze");
		
		initScore();
		
		initBoard();	
		
		initButtons();
		
		shell.open();
	}
	
	private void initButtons() 
	{
		Button btn;
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Undo Move");
		btn.setLayoutData(new GridData(SWT.LEFT,SWT.FILL, false, false, 1,1));
		btn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				execCommand(CommandsConsts.VIEW_MOVE_UNDO);
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Restart Game");
		btn.setLayoutData(new GridData(SWT.LEFT,SWT.FILL, false, false, 1,1));
		btn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				execCommand(CommandsConsts.VIEW_TEC_INITIATE_BOARD);
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Save Game");
		btn.setLayoutData(new GridData(SWT.LEFT,SWT.FILL, false, false, 1,1));
		btn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				 FileDialog dialog = new FileDialog(shell, SWT.SAVE);
				 dialog.setFilterNames(new String[] {"Xml Files"});
				 dialog.setFilterExtensions(new String[] {"*.xml"});
				 String path = dialog.open();
				 if(path==null)
				 {
					 board.setFocus();
					 return;
				 }
				 execCommand(CommandsConsts.VIEW_TEC_SAVE_BOARD,path);
			}


			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Load Game");
		btn.setLayoutData(new GridData(SWT.LEFT,SWT.FILL, false, false, 1,1));
		btn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				 FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				 dialog.setFilterNames(new String[] {"Xml Files"});
				 dialog.setFilterExtensions(new String[] {"*.xml"});
				 String path = dialog.open();
				 if(path==null)
				 {
					 board.setFocus();
					 return;
				 }
				 execCommand(CommandsConsts.VIEW_TEC_LOAD_BOARD,path);
			}


			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
	}

	private void initScore() 
	{
		score = new Label(shell, SWT.BOLD);
		score.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false, 1,1));
	}

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

	@Override
	public int getUserCommand() 
	{
		return this.userCommand;
	}
	private void setUserCommand(int userCommand)
	{
		this.userCommand = userCommand;
	}
	
	private void initBoard()
	{
		Board board = new BoardMaze(shell, SWT.BORDER);
		board.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true, 1,50));
		this.board = board;
		
		setBoardKeyEvents();
		setBoardDrop();
		
		this.execCommand(CommandsConsts.VIEW_TEC_INITIATE_BOARD);
	}
	
	private void execCommand(int command) 
	{
		execCommand(command,null);
	}
	
	private void execCommand(int command,Object data) 
	{
		this.setUserCommand(command);
		setChanged();
		notifyObservers(data);
		this.board.setFocus();
	}

	private void setBoardKeyEvents()
	{
		this.board.addKeyListener(new KeyListener() 
			{
				
				@Override
				public void keyReleased(KeyEvent e) 
				{
					
				}
				
				@Override
				public void keyPressed(KeyEvent e) 
				{
					
					boolean isCtrl = (e.stateMask & SWT.CTRL) != 0;
					boolean isShift = (e.stateMask & SWT.SHIFT) != 0;
					if(isCtrl && isShift)//decided its illegal.
					{
						return;
					}
					
					switch(e.keyCode)
					{
					case SWT.ARROW_UP:
						if(isCtrl || isShift)//decided its illegal.
							return;
						execCommand(CommandsConsts.VIEW_MOVE_UP);
					break;
					case SWT.ARROW_DOWN:
						if(isCtrl || isShift)//decided its illegal.
							return;
						execCommand(CommandsConsts.VIEW_MOVE_DOWN);
					break;
					case SWT.ARROW_LEFT:
						if(isShift)
							execCommand(CommandsConsts.VIEW_MOVE_UP_LEFT);
						else if(isCtrl)
							execCommand(CommandsConsts.VIEW_MOVE_DOWN_LEFT);
						else 
							execCommand(CommandsConsts.VIEW_MOVE_LEFT);
					break;
					case SWT.ARROW_RIGHT:
						if(isShift)
							execCommand(CommandsConsts.VIEW_MOVE_UP_RIGHT);
						else if(isCtrl)
							execCommand(CommandsConsts.VIEW_MOVE_DOWN_RIGHT);
						else 
							execCommand(CommandsConsts.VIEW_MOVE_RIGHT);
					break;
					case 'z':
					case 'Z':
						execCommand(CommandsConsts.VIEW_MOVE_UNDO);
					default://no valid command.
						return;
					}
				}
			});
	}

	@Override
	public void addObserver(Presenter presenter) 
	{
		super.addObserver(presenter);
	}

	@Override
	public void displayScore(final String scoreStr) 
	{
		display.syncExec(new Runnable() {
			
			@Override
			public void run() 
			{
				score.setText("Score: "+scoreStr);
				score.setToolTipText(scoreStr);
			}
		});
	}
	
	private void setBoardDrop()
	{
		DropTarget dt = new DropTarget(this.board, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
		dt.setTransfer(new Transfer[]{TextTransfer.getInstance()});
		dt.addDropListener(new DropTargetAdapter() {
			
			
			int startX = 0;
			int endX = 0;
			int startY = 0;
			int endY = 0;
			@Override
			public void dragLeave(DropTargetEvent e) 
			{
			}
			
			@Override
			public void dropAccept(DropTargetEvent arg0) {}
			
			@Override
			public void drop(DropTargetEvent e) 
			{
				int colLen = board.getBoardData().length;
				int rowLen = board.getBoardData()[0].length;
				int squareWidth = (board.getBounds().width-5)/rowLen;
				int squareHeight = (board.getBounds().height-5)/colLen;
				//DO NOTHING. JUST FOR THE EFFECTS.
				int xSquares =rowLen*(endX - startX)/((rowLen-1)*squareWidth);
				int ySquares =colLen*(endY - startY)/((colLen-1)*squareHeight);
				
				switch(xSquares)
				{
					case 0:
						switch(ySquares)
						{
							case -1:
								execCommand(CommandsConsts.VIEW_MOVE_UP);
								break;
							case 1:
								execCommand(CommandsConsts.VIEW_MOVE_DOWN);
								break;
						}
						break;
					case 1:
						switch(ySquares)
						{
							case 0:
								execCommand(CommandsConsts.VIEW_MOVE_RIGHT);
								break;
							case 1:
								execCommand(CommandsConsts.VIEW_MOVE_DOWN_RIGHT);
								break;
							case -1:
								execCommand(CommandsConsts.VIEW_MOVE_UP_RIGHT);
								break;
						}
						break;
					case -1:
						switch(ySquares)
						{
							case 0:
								execCommand(CommandsConsts.VIEW_MOVE_LEFT);
								break;
							case 1:
								execCommand(CommandsConsts.VIEW_MOVE_DOWN_LEFT);
								break;
							case -1:
								execCommand(CommandsConsts.VIEW_MOVE_UP_LEFT);
								break;
						}
						break;
				}
					
			}
			
			@Override
			public void dragOver(DropTargetEvent e) 
			{
				endX = e.x;
				endY = e.y;
			}
			
			@Override
			public void dragOperationChanged(DropTargetEvent arg0) {}
			

			
			@Override
			public void dragEnter(DropTargetEvent e) 
			{
				
				startX = e.x;
				startY = e.y;
			}
		});
	}
}
