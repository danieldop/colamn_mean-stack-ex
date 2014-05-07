package mvp.interfaces.view;

import java.util.Observable;

import mvp.consts.CommandsConsts;
import mvp.consts.GeneralConsts;
import mvp.controller.Presenter;
import mvp.interfaces.view.objects.Board;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;


public abstract class ViewAbs extends Observable implements View 
{
	protected Display display;
	protected Shell shell;
	protected Board board;//extends Canvas widget.
	protected Label score;
	
	protected int userCommand = -1;//command for the Presenter.
	
	
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
 <br>
	 * initiates display,shell<br>
	 * initiates score,board<br>
	 * initiates buttons<br>
	 * initiates menu bar.<br>
	 * opens shell when done.
	 */
	protected void initComponents()
	{
		
		display = new Display();

		this.shell = initShell();
		
		initImage();
		
		this.score = initScore();
		
		this.board = initBoard();	
		
		initBoardKeyEvents();
		initBoardMouseEvents();
		
		initButtons();
		
		initMenuBar();
		
		shell.open();
		execCommand(CommandsConsts.VIEW_TEC_INITIATE_BOARD,true);
	}
	
	protected void initButtons() 
	{
		Button btn;
		
		btn=new Button(shell, SWT.PUSH);		
		
		btn.setText("Restart Game");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(openMessageBox(GeneralConsts.VIEW_TITLE_RESTART,GeneralConsts.VIEW_MGS_ARE_YOU_SURE_RESTART,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
					execCommand(CommandsConsts.VIEW_TEC_RESTART_BOARD,true);
				else
					board.setFocus();//retrieve board focus.
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Undo Move");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				execCommand(CommandsConsts.VIEW_MOVE_UNDO,true);
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Reset Settings");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(openMessageBox(GeneralConsts.VIEW_TITLE_RESET_SETTINGS ,GeneralConsts.VIEW_MGS_ARE_YOU_SURE_RESET_SETTINGS,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
				{
					resetGameSettings();
				}
				else
					board.setFocus();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Save Game");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				saveOrLoadGame(SWT.SAVE);
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		btn=new Button(shell, SWT.PUSH);
		btn.setText("Load Game");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				saveOrLoadGame(SWT.OPEN);
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});

		btn=new Button(shell, SWT.PUSH);
		btn.setText("Exit");
		btn.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false, false, 1,1));
		btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(openMessageBox(GeneralConsts.VIEW_TITLE_EXIT,GeneralConsts.VIEW_MGS_ARE_YOU_SURE_EXIT,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
					shell.dispose();
				else
					board.setFocus();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
	}
	
	/**
	 * @param command
	 * @param focusBoard
	 * focusBoard is used when calling execCommand from a thread different from view thread.
	 */
		protected void execCommand(int command,boolean focusBoard) 
		{
			execCommand(command,null,focusBoard);

		}
		/**
		 * @param command
		 * @param focusBoard
		 * focusBoard is used when calling execCommand from a thread different from view thread.
		 */
		protected void execCommand(int command,Object data,boolean focusBoard) 
		{
			this.setUserCommand(command);
			setChanged();
			notifyObservers(data);
			if(focusBoard)
				this.board.setFocus();
		}

		
		private void setUserCommand(int command) 
		{
			this.userCommand = command;
		}
		@Override
		public void addObserver(Presenter presenter) 
		{
			super.addObserver(presenter);
		}
		@Override
		public int getUserCommand() 
		{
			return this.userCommand;
		}
		
		private void initImage()
		{
			
			Image icon = null;
			try
			{
				icon = new Image(display,new ImageData(getClass().getResourceAsStream(getIconPath())));
				shell.setImage(icon);
			}
			finally
			{
				if(icon!= null)
					icon.dispose();
			}
		}
		
		protected int openMessageBox(String title,String text,int style)
		{
			MessageBox mb = new MessageBox(shell,style);
			mb.setText(title);
			mb.setMessage(text);
			return mb.open();
		}
		
		protected void saveOrLoadGame(int dialogType)
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
					if(openMessageBox(GeneralConsts.VIEW_TITLE_EXIT,GeneralConsts.VIEW_MGS_ARE_YOU_SURE_EXIT,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
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
					if(openMessageBox(GeneralConsts.VIEW_TITLE_RESTART,GeneralConsts.VIEW_MGS_ARE_YOU_SURE_RESTART,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
						execCommand(CommandsConsts.VIEW_TEC_RESTART_BOARD,false);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});
			
		    MenuItem editResetItem = new MenuItem(editMenu, SWT.PUSH);
		    editResetItem.setText("Reset Settings");
		    editResetItem.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) 
				{				
					if(openMessageBox(GeneralConsts.VIEW_TITLE_RESET_SETTINGS,GeneralConsts.VIEW_MGS_ARE_YOU_SURE_RESET_SETTINGS,SWT.ICON_QUESTION | SWT.YES | SWT.NO) == SWT.YES)
						resetGameSettings();
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
		
		
		protected abstract Shell initShell();
		protected Label initScore()
		{
			Label score = new Label(shell, SWT.BOLD);
			score.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false, 1,1));
			return score;
		}
		protected abstract Board initBoard();
		protected abstract void initBoardMouseEvents();
		protected abstract void initBoardKeyEvents();
		protected abstract String getIconPath();
		protected abstract void resetGameSettings();
		
		protected void isGameEnded() 
		{
			execCommand(CommandsConsts.VIEW_TEC_IS_WON_GAME,false);
			execCommand(CommandsConsts.VIEW_TEC_IS_LOST_GAME,false);
		}
		
}
