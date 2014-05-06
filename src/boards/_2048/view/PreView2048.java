package boards._2048.view;

import java.util.Observable;

import mvp.controller.Presenter;
import mvp.interfaces.view.View;
import mvp.interfaces.view.objects.Board;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import boards._2048.model.Model2048;
import boards._2048.view.objects.Board2048;

public class PreView2048 extends Observable  implements View {
	private Display display;
	private Shell shell;
	private Board board;
	private Text size;
	private int userCommand = -1;
	private Button continueBtn;

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
		
		shell.setLayout(new GridLayout(4, false));
		shell.setSize(500, 500);
		shell.setLocation(display.getClientArea().width/2-250,display.getClientArea().height/2-250);
		shell.setMinimumSize(500,500);
		shell.setText("Set Maze Settings");
		
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
		continueBtn.setLayoutData(new GridData(SWT.LEFT,SWT.LEFT, false, false, 2,0));
		continueBtn.addMouseListener(new MouseListener() 
		{
			Integer sizeNum;

			boolean invalidCall;
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				if(!invalidCall)
				{
					Presenter.present(new Model2048(sizeNum),new View2048());
					shell.dispose();
				}		
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) 
			{
				invalidCall = false;
				continueBtn.setEnabled(false);
				if(!isNumeric(size.getText()))
				{
					invalidCall = true;
					continueBtn.setEnabled(true);
					return;
				}
				sizeNum = Integer.valueOf(size.getText());
			
				size.setText(String.valueOf(sizeNum));
//				cols.setText(String.valueOf(colsNum));
				
//				Integer dataHelper = sizeNum;
//				execCommand(CommandsConsts.VIEW_TEC_BOARD_PREVIEW,dataHelper);
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
			Label x = new Label(shell,SWT.NONE);
			x.setText("d");
			x.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false, 1,1));
				
			size = new Text(shell,SWT.BORDER);
			gc = new GC(size);
			
			GridData g = new GridData(SWT.FILL,SWT.TOP, false, false, 1,1);
			g.widthHint = gc.getFontMetrics().getAverageCharWidth()*3;
			
			size.setLayoutData(g);
			size.setTextLimit(2);
		
		
//		
//		cols = new Text(shell,SWT.BORDER);
//		cols.setLayoutData(g);
//		cols.setTextLimit(2);
		
		size.addKeyListener(this.getKeyListener());
//		cols.addKeyListener(this.getKeyListener());
		
		size.setSelection(size.getText().length());
		
		
//		Label level = new Label(shell,SWT.NONE);
//		level.setText("Level: ");
//		level.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false, 1,1));
//		
//		levels = new Combo(shell, SWT.READ_ONLY);
//		levels.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false, 2,1));
//		levels.add(GeneralConsts.VIEW_LEVEL_ONE);
//		levels.add(GeneralConsts.VIEW_LEVEL_TWO);
//		levels.add(GeneralConsts.VIEW_LEVEL_THREE);
//		levels.add(GeneralConsts.VIEW_LEVEL_FOUR);
//		levels.add(GeneralConsts.VIEW_LEVEL_FIVE);
//		levels.select(0);
		
		}
		finally
		{
			if(gc!=null)
				gc.dispose();
		}
	}
	
	private void initBoard()
	{
		Board board = new Board2048(shell, SWT.BORDER);
		board.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true, 1,50));
		this.board = board;
	}
	
//	@Override
//	public void displayScore(String score) 
//	{
//		//DO NOTHING. GAME HASNT STARTED YET.
//	}
	
	private KeyListener getKeyListener()
	{
		return new KeyListener() 
		{
			private Text source;
			@Override
			public void keyReleased(KeyEvent arg0) 
			{
		
				String sizeStr = size.getText();

				if(!isNumeric(sizeStr))
				{
				
					sizeStr = "";
				}

				
				if(sizeStr.length()*sizeStr.length() == 0)
				{
					 sizeStr = "0";
				}
				

				Integer sizeNum = Integer.valueOf(sizeStr);
				
				Integer dataHelper;
				if(sizeNum == 0)
				{
					dataHelper = 0;
				}
				else
				{ 
//					if(rowsNum>GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS)
//						rowsNum = GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS;
//					if(colsNum>GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS)
//						colsNum = GeneralConsts.VIEW_MAZE_MAX_ROWS_COLS;
					
					size.setText(String.valueOf(sizeNum));
					
					
					continueBtn.setEnabled(true);
				}
				this.source.setSelection(this.source.getText().length());
				dataHelper = sizeNum;
//				execCommand(CommandsConsts.VIEW_TEC_BOARD_PREVIEW,dataHelper);
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) 
			{
				this.source = (Text)arg0.getSource();
			}
		};
	}

	@Override
	public void displayScore(String score) 
	{
		display.syncExec(new Runnable() {
			
			@Override
			public void run() 
			{
				//score.setText(score);
			}
		});
		
	}

}
