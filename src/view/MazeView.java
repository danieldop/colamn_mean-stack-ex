package view;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import model.MazeModel;

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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
	private boolean isUp,isLeft,isDown,isRight;
	private void initComponents()
	{
		display = new Display();
		shell = new Shell(display);
		
		shell.setLayout(new GridLayout(2, false));
		shell.setSize(500, 500);
		shell.setMinimumSize(500,500);
		shell.setLocation(display.getClientArea().width/2-250,display.getClientArea().height/2-250);
		shell.setText("Maze");
		
		initScore();
		
		initBoard();	
		
		initButtons();
		
		initMenuBar();
		
		shell.open();
	}
	
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
			{execCommand(CommandsConsts.VIEW_TEC_INITIATE_BOARD,true);}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
	    MenuItem editResetItem = new MenuItem(editMenu, SWT.PUSH);
	    editResetItem.setText("Reset Maze Settings");
	    editResetItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{				
				Presenter.present(new MazeModel(),new MazePreView());
				shell.dispose();
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
				//ARE YOU SURE POP UP.
				execCommand(CommandsConsts.VIEW_TEC_INITIATE_BOARD,true);
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
				//ARE YOU SURE POP UP.
				Presenter.present(new MazeModel(),new MazePreView());
				shell.dispose();
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
			public void mouseUp(MouseEvent arg0) {shell.dispose();}


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
		board.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true, 1,8));
		this.board = board;
		
		setBoardKeyEvents();
		setBoardDrop();
		
		this.execCommand(CommandsConsts.VIEW_TEC_INITIATE_BOARD,true);
	}
	
	private void execCommand(int command,boolean focusBoard) 
	{
		execCommand(command,null,focusBoard);

	}
	
	private void execCommand(int command,Object data,boolean focusBoard) 
	{
		this.setUserCommand(command);
		setChanged();
		notifyObservers(data);
		if(focusBoard)
			this.board.setFocus();
	}

	private void setBoardKeyEvents()
	{
		final TimerTask test1 = new TimerTask() 
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
		final Timer t = new Timer();
		t.scheduleAtFixedRate(test1,22,65);
		this.board.addKeyListener(new KeyListener() 
			{
				
				@Override
				public void keyReleased(KeyEvent e) 
				{
					setArrowKey(false,e.keyCode);
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
					if(state)
					{
						
					}
				}
				
				@Override
				public void keyPressed(KeyEvent e) 
				{
					boolean isCtrl = (e.stateMask & SWT.CTRL) != 0;
					setArrowKey(true,e.keyCode);
					switch(e.keyCode)
					{
					case 'z':
					case 'Z':
						if(isCtrl)
							execCommand(CommandsConsts.VIEW_MOVE_UNDO,false);
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
				int xSquares =3*(endX - startX)/(2*squareWidth);
				int ySquares =3*(endY - startY)/(2*squareHeight);
				switch(xSquares)
				{
					case 0:
						switch(ySquares)
						{
							case -1:
								execCommand(CommandsConsts.VIEW_MOVE_UP,true);
								break;
							case 1:
								execCommand(CommandsConsts.VIEW_MOVE_DOWN,true);
								break;
						}
						break;
					case 1:
						switch(ySquares)
						{
							case 0:
								execCommand(CommandsConsts.VIEW_MOVE_RIGHT,true);
								break;
							case 1:
								execCommand(CommandsConsts.VIEW_MOVE_DOWN_RIGHT,true);
								break;
							case -1:
								execCommand(CommandsConsts.VIEW_MOVE_UP_RIGHT,true);
								break;
						}
						break;
					case -1:
						switch(ySquares)
						{
							case 0:
								execCommand(CommandsConsts.VIEW_MOVE_LEFT,true);
								break;
							case 1:
								execCommand(CommandsConsts.VIEW_MOVE_DOWN_LEFT,true);
								break;
							case -1:
								execCommand(CommandsConsts.VIEW_MOVE_UP_LEFT,true);
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
}
