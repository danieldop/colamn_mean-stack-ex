package view;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import model.MazeModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import view.objects.Board;
import view.objects.BoardMaze;
import consts.CommandsConsts;
import consts.GeneralConsts;
import controller.Presenter;

public class MazeView extends Observable implements View
{
	private Display display;
	private Shell shell;
	private Board board;//extends Canvas widget.
	private Label score;
	
	private int userCommand = -1;//command for the Presenter.
	
	
	private boolean isUp,isLeft,isDown,isRight;//isUp=>is arrow up key is down., etc...
	
	boolean isMouseDown = false;//for board mouse event if user clicked on the mouse.
	
	/**
	 * @author omerpr <br>
	 * initiates display,shell<br>
	 * initiates score,board<br>
	 * initiates buttons<br>
	 * initiates menu bar.<br>
	 * opens shell when done.
	 */
	private void initComponents()
	{
		
		display = new Display();
		
		shell = new Shell(display);
		
		shell.setLayout(new GridLayout(2, false));
		shell.setSize(500, 500);
		shell.setMinimumSize(500,500);
		shell.setLocation(display.getClientArea().width/2-250,display.getClientArea().height/2-250);
		shell.setText("Maze");
		
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
		
		initScore();
		
		initBoard();	
		
		initButtons();
		
		initMenuBar();
		
		shell.open();
		
	}
	/**
	 * @author omerpr<br>
	 * menubar structure=> File,Edit<br>
	 * menubar features:<br>
	 * File:{Save Game,Load Game,Exit}<br>
	 * Edit:{Restart,Reset settings,Undo}
	 */
	private void initMenuBar() 
	{
		Menu menuBar = new Menu(shell, SWT.BAR);
		
		
		/*FILE MENU BAR SECTION - START*/
		MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("&File");	
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileMenuHeader.setMenu(fileMenu);
	    
	    MenuItem fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileSaveItem.setText("Save Game");
	    fileSaveItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				saveOrLoadGame(SWT.SAVE);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
	    
	    MenuItem fileLoadItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileLoadItem.setText("Load Game");
	    fileLoadItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				saveOrLoadGame(SWT.OPEN);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});

	    MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileExitItem.setText("Exit");
	    fileExitItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(openMessageBox(GeneralConsts.VIEW_MGS_ARE_YOU_SURE_EXIT,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
					shell.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
	    /*FILE MENU BAR SECTION - END*/
	    
	    /*EDIT MENU BAR SECTION - START*/
		MenuItem editMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		editMenuHeader.setText("&Edit");	
		Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
		editMenuHeader.setMenu(editMenu);
	    
		MenuItem editRestartItem = new MenuItem(editMenu, SWT.PUSH);
		editRestartItem.setText("Restart Maze");
		editRestartItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(openMessageBox(GeneralConsts.VIEW_MGS_ARE_YOU_SURE_RESTART,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
					execCommand(CommandsConsts.VIEW_TEC_INITIATE_BOARD,false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
	    MenuItem editResetItem = new MenuItem(editMenu, SWT.PUSH);
	    editResetItem.setText("Reset Maze Settings");
	    editResetItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{				
				if(openMessageBox(GeneralConsts.VIEW_MGS_ARE_YOU_SURE_RESET_SETTINGS,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
				{
					Presenter.present(new MazeModel(),new MazePreView());
					shell.dispose();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
	    
	    
	    MenuItem editUndoItem = new MenuItem(editMenu, SWT.PUSH);
	    editUndoItem.setText("Undo Move{Ctrl+z}");
	    editUndoItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {execCommand(CommandsConsts.VIEW_MOVE_UNDO,true);}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
	    
	    /*EDIT MENU BAR SECTION - END*/
	    
	    shell.setMenuBar(menuBar);
		
		
	}

	private void initButtons() 
	{
		Button btn;
		
		btn=new Button(shell, SWT.PUSH);		
		
		btn.setText("Restart Game");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				if(openMessageBox(GeneralConsts.VIEW_MGS_ARE_YOU_SURE_RESTART,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
					execCommand(CommandsConsts.VIEW_TEC_INITIATE_BOARD,true);
				else
					board.setFocus();//retrieve board focus.
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Undo Move");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				execCommand(CommandsConsts.VIEW_MOVE_UNDO,true);
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Reset Maze Settings");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				if(openMessageBox(GeneralConsts.VIEW_MGS_ARE_YOU_SURE_RESET_SETTINGS,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
				{
					Presenter.present(new MazeModel(),new MazePreView());
					shell.dispose();
				}
				else
					board.setFocus();
			}


			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Save Game");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				saveOrLoadGame(SWT.SAVE);
			}


			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Load Game");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				saveOrLoadGame(SWT.OPEN);
			}


			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Exit");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				if(openMessageBox(GeneralConsts.VIEW_MGS_ARE_YOU_SURE_EXIT,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
					shell.dispose();
				else
					board.setFocus();
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

	/**
	 * @author omerpr
	 * @param int[] data =>maze to display on the canvas.
	 * calls the redraw of the canvas(on a synch Execution call) 
	 *
	 */
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
	
	/**
	 * @author omerpr
	 * @
	 * init the canvas board.
	 * @
	 * init the canvas board events.
	 */
	private void initBoard()
	{
		Board board = new BoardMaze(shell, SWT.BORDER);
		board.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true, 1,8));
		this.board = board;
		
		setBoardKeyEvents();
		setBoardMoveEvents();
		
		this.execCommand(CommandsConsts.VIEW_TEC_INITIATE_BOARD,true);
	}
	
	/**
	 * @author omerpr
	 * @ 
	 * enables mouse being dragged on the board.
	 * @ 
	 * 
	 * NOTICE: board.getData("mousePoint") takes the mouse coordinates on the board from the board Object.
	 * This structure is used inorder to keep the modularisation intact.
	 */
	private void setBoardMoveEvents() 
	{
		board.addMouseListener(new MouseListener() {
			Point mouse;
			int squareWidth,squareHeight;
			Cursor hand = new Cursor(display, SWT.CURSOR_SIZEALL);
			Cursor defaultCursor = new Cursor(display,SWT.CURSOR_ARROW);
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				isMouseDown = false;
				board.setCursor(defaultCursor);
			}
			
			@Override
			public void mouseDown(MouseEvent e) 
			{
				mouse = (Point)board.getData("mousePoint");//extracting the mouse current location.
				
				squareWidth = (board.getBounds().width-5)/board.getBoardData()[0].length;
				squareHeight = (board.getBounds().height-5)/board.getBoardData().length;
				if(mouse!=null)
				{
					if(e.x>=mouse.x && e.x<mouse.x+squareWidth && e.y>=mouse.y && e.y<mouse.y+squareHeight)
					{
						isMouseDown = true;
						board.setCursor(hand);
					}
				}
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		board.addMouseMoveListener(new MouseMoveListener() 
		{
			Point mouse;
			int squareWidth,squareHeight;//xSquares=>how many squares from mouse x,ySquares=>how many squares from mouse y.
			int mouseX,mouseY;
			
			/**
			 * 
			 * @param double num1
			 * @param double num2
			 * detects the closest move the user is trying to move to and preforms the movement action.
			 */
			private void preformClosestMove(double x,double y)
			{
				if(x>1.6 || x<-1.6 || y>1.6 || y<-1.6)
					return;//not relevant.
				
				if(x<-0.5 && y<-0.5)
					execCommand(CommandsConsts.VIEW_MOVE_UP_LEFT, false);
				else if(x>0.5 && y<-0.5)
					execCommand(CommandsConsts.VIEW_MOVE_UP_RIGHT, false);
				else if(x<-0.5 && y>0.5)
					execCommand(CommandsConsts.VIEW_MOVE_DOWN_LEFT, false);
				else if(x>0.5 && y>0.5)
					execCommand(CommandsConsts.VIEW_MOVE_DOWN_RIGHT, false);
				else if(x<-0.85 && y>-0.2)
					execCommand(CommandsConsts.VIEW_MOVE_LEFT, false);
				else if(x>0.85 && y<0.2)
					execCommand(CommandsConsts.VIEW_MOVE_RIGHT, false);
				else if(y<-0.85 && x>-0.2)
					execCommand(CommandsConsts.VIEW_MOVE_UP, false);
				else if(y>0.85 && x<0.2)
					execCommand(CommandsConsts.VIEW_MOVE_DOWN, false);
			}
			
			@Override
			public void mouseMove(MouseEvent e) 
			{
				if(isMouseDown)
				{
					mouse = (Point)board.getData("mousePoint");
					if(mouse != null)
					{
						
						squareWidth = (board.getBounds().width-5)/board.getBoardData()[0].length;
						squareHeight = (board.getBounds().height-5)/board.getBoardData().length;
						
						mouseX=mouse.x+squareWidth/2;
						mouseY=mouse.y+squareHeight/2;
		
						preformClosestMove(((double)e.x-mouseX)/(squareWidth),((double)e.y-mouseY)/(squareHeight));
					}
					
				}
			}
		});
	}
/**
 * @author omerpr
 * @param command
 * @param focusBoard
 * focusBoard is used when calling execCommand from a thread different from view thread.
 */
	private void execCommand(int command,boolean focusBoard) 
	{
		execCommand(command,null,focusBoard);

	}
	/**
	 * @author omerpr
	 * @param command
	 * @param focusBoard
	 * focusBoard is used when calling execCommand from a thread different from view thread.
	 */
	private void execCommand(int command,Object data,boolean focusBoard) 
	{
		this.setUserCommand(command);
		setChanged();
		notifyObservers(data);
		if(focusBoard)
			this.board.setFocus();
	}

	/**
	 * setting up all key movement responds.
	 * @
	 * ARROW_UP => UP
	 * @
	 * ARROW_DOWN => DOWN
	 * @
	 * ETC
	 * .
	 * .
	 */
	
	private void setBoardKeyEvents()
	{
		this.board.addKeyListener(new KeyListener() 
			{
				int counter = 0;
				Timer timer = null;
				@Override
				public void keyReleased(KeyEvent e) 
				{
					setArrowKey(false,e.keyCode);
					if(timer!=null)
						timer.cancel();
					timer = null;
				}
				
				private void setArrowKey(boolean state,int keyCode)
				{
					switch(keyCode)
					{
					case SWT.ARROW_UP:
						isUp = state;
					break;
					case SWT.ARROW_DOWN:
						isDown = state;
					break;
					case SWT.ARROW_LEFT:
						isLeft = state;
					break;
					case SWT.ARROW_RIGHT:
						isRight = state;
					break;
					}
				}
				
				@Override
				public void keyPressed(KeyEvent e) 
				{
					setArrowKey(true,e.keyCode);
					switch(e.keyCode)
					{
					case 'z':
					case 'Z':
						if((e.stateMask & SWT.CTRL) != 0)//ctrl is down.
							execCommand(CommandsConsts.VIEW_MOVE_UNDO,false);
					}
					if(timer==null)//a movement has not been started yet.
					{
						counter = 0;
						timer = new Timer();
						timer.schedule(getMovementTask(),30);//give 30 milliseconds to include diagonal move.
					}
					else//a movement is already scheduled.
					{
						if(counter>8)
							counter = 0;
						timer.schedule(getMovementTask(),100+ (++counter)*70);//schedule the next move to occur later.
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
	
	private void saveOrLoadGame(int dialogType)
	{
		 FileDialog dialog = new FileDialog(shell, dialogType);
		 dialog.setFilterNames(new String[] {"Xml Files"});
		 dialog.setFilterExtensions(new String[] {"*.xml"});
		 String path = dialog.open();
		 int userCommand = (dialogType == SWT.SAVE) 
				 			?CommandsConsts.VIEW_TEC_SAVE_BOARD 
				 			:CommandsConsts.VIEW_TEC_LOAD_BOARD;
		 if(path==null)
		 {
			 board.setFocus();
			 return;
		 }
		 execCommand(userCommand,path,true);
	}
	
	
	private TimerTask getMovementTask()
	{
		 return new TimerTask() 
			{
				@Override
				public void run() 
				{
					if((isUp && isDown) || (isLeft && isRight))//Illegal
						return;
						
					if(isUp)
					{
						if(isLeft)
							execCommand(CommandsConsts.VIEW_MOVE_UP_LEFT,false);
						else if(isRight)
							execCommand(CommandsConsts.VIEW_MOVE_UP_RIGHT,false);
						else
							execCommand(CommandsConsts.VIEW_MOVE_UP,false);
					}
					else if(isDown)
					{
						if(isLeft)
							execCommand(CommandsConsts.VIEW_MOVE_DOWN_LEFT,false);
						else if(isRight)
							execCommand(CommandsConsts.VIEW_MOVE_DOWN_RIGHT,false);
						else
							execCommand(CommandsConsts.VIEW_MOVE_DOWN,false);
					}
					else if(isRight)
						execCommand(CommandsConsts.VIEW_MOVE_RIGHT,false);
					else if(isLeft)
						execCommand(CommandsConsts.VIEW_MOVE_LEFT,false);
				}
			};
	}
	
	private int openMessageBox(String text,int style)
	{
		MessageBox mb = new MessageBox(shell,style);
		mb.setMessage(text);
		return mb.open();
	}
}
