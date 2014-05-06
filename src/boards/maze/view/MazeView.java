package boards.maze.view;

import java.util.Timer;
import java.util.TimerTask;

import mvp.consts.CommandsConsts;
import mvp.controller.Presenter;
import mvp.interfaces.view.ViewAbs;
import mvp.interfaces.view.objects.Board;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

import boards.maze.model.MazeModel;
import boards.maze.view.objects.BoardMaze;

public class MazeView extends ViewAbs
{
	
	private boolean isUp,isLeft,isDown,isRight;//isUp=>is arrow up key is down., etc...
	
	boolean isMouseDown = false;//for board mouse event if user clicked on the mouse.


	
	/**
	 * @author omerpr
	 * @
	 * init the canvas board.
	 * @
	 * init the canvas board events.
	 */
	protected Board initBoard()
	{
		Board board = new BoardMaze(shell, SWT.BORDER);
		board.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true, 1,8));

		return board;
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
	@Override
	protected void initBoardMouseEvents() 
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
				if(mouse!=null)
				{
					squareWidth = (board.getBounds().width-5)/board.getBoardData()[0].length;
					squareHeight = (board.getBounds().height-5)/board.getBoardData().length;
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
	@Override
	protected void initBoardKeyEvents()
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

	@Override
	protected Shell initShell() 
	{
		
		Shell shell = new Shell(display);
		
		shell.setLayout(new GridLayout(2, false));
		shell.setSize(500, 500);
		shell.setMinimumSize(500,500);
		shell.setLocation(display.getClientArea().width/2-250,display.getClientArea().height/2-250);
		shell.setText("Maze");
		
		return shell;
	}

	@Override
	protected String getIconPath() {
		// TODO Auto-generated method stub
		return "/images/MAZE.jpg";
	}

	@Override
	protected void resetGameSettings() 
	{
		new Presenter(new MazeModel(),new MazePreView());
		shell.dispose();
	}
}
