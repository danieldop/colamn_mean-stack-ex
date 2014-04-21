package view;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import consts.CommandsConsts;
import controller.Presenter;

public class MazeView extends Observable implements View
{
	private Display display;
	private Shell shell;
	private Board board;
	private int userCommand;

	private void initComponents()
	{
		display = new Display();
		shell = new Shell(display);
		
		
		
		shell.setLayout(new GridLayout(2, false));
		shell.setSize(300, 350);
		shell.setText("my 2048 game");
		Button b1=new Button(shell, SWT.PUSH);
		b1.setText("button 1");
		b1.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false, 1,1));
		Button b2=new Button(shell, SWT.PUSH);
		b2.setText("button 2");
		b2.setLayoutData(new GridData(SWT.LEFT,SWT.TOP, false, false, 1,1));

		initBoard();
		
		setKeyEvents();
		
		shell.open();
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
		board.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true, 2,2));
		this.board = board;
		
		this.setUserCommand(CommandsConsts.INITIATE);
		this.refresh();
	}
	private void refresh() 
	{
		setChanged();
		notifyObservers();
		this.board.setFocus();
	}

	private void setKeyEvents()
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
						setUserCommand(CommandsConsts.UP);
					break;
					case SWT.ARROW_DOWN:
						if(isCtrl || isShift)//decided its illegal.
							return;
						setUserCommand(CommandsConsts.DOWN);
					break;
					case SWT.ARROW_LEFT:
						if(isShift)
							setUserCommand(CommandsConsts.UP_LEFT);
						else if(isCtrl)
							setUserCommand(CommandsConsts.DOWN_LEFT);
						else 
							setUserCommand(CommandsConsts.LEFT);
					break;
					case SWT.ARROW_RIGHT:
						if(isShift)
							setUserCommand(CommandsConsts.UP_RIGHT);
						else if(isCtrl)
							setUserCommand(CommandsConsts.DOWN_RIGHT);
						else 
							setUserCommand(CommandsConsts.RIGHT);
					break;
					default://no valid command.
						return;
					}
					refresh();
				}
			});
	}

	@Override
	public void addObserver(Presenter presenter) 
	{
		super.addObserver(presenter);
	}
}
