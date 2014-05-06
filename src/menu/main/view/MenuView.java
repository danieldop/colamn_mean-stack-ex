package menu.main.view;

import java.util.Observable;

import mvp.consts.GeneralConsts;
import mvp.controller.Presenter;
import mvp.interfaces.view.View;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import boards._2048.model.Model2048;
import boards._2048.view.PreView2048;
import boards.maze.model.MazeModel;
import boards.maze.view.MazePreView;

public class MenuView extends Observable implements View{
	private Display display;
	private Shell shell;
	private int userCommand = -1;
	private Button nextBtn;
	private Combo apps;
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
		shell.setLocation(display.getClientArea().width/2-250,display.getClientArea().height/2-250);
		shell.setMinimumSize(500,500);
		shell.setText("Choose Application");
		
		/*Image icon = null;
		try
		{
			icon = new Image(display,new ImageData(getClass().getResourceAsStream("/images/MAZE.jpg")));
			shell.setImage(icon);
		}
		finally
		{
			if(icon!= null)
				icon.dispose();
		}*/
		
		initDataSection();

		initNextButton();
		shell.open();
	}

	private void initNextButton() 
	{
		nextBtn = new Button(shell,SWT.PUSH);
		//continueBtn.setEnabled(false);
		nextBtn.setText("Continue");
		nextBtn.setLayoutData(new GridData(SWT.LEFT,SWT.LEFT, false, false, 1,0));
		nextBtn.addMouseListener(new MouseListener() 
		{
			String selectedApp;
			@Override
			public void mouseUp(MouseEvent arg0) 
			{
				selectedApp = apps.getText();
				switch(selectedApp)
				{
					case GeneralConsts.VIEW_2048_APP_NAME:
						Presenter.present(new Model2048(4),new PreView2048());
						shell.dispose();
						break;
					case GeneralConsts.VIEW_MAZE_APP_NAME:
						Presenter.present(new MazeModel(), new MazePreView());
						shell.dispose();
						break;
					default:
						//SHOW MESSAGE.
				}
				
				
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) 
			{
				
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
	
	@Override
	public void addObserver(Presenter p) 
	{
		super.addObserver(p);
	}



	private void initDataSection() 
	{
		apps = new Combo(shell,SWT.READ_ONLY);
		apps.add(GeneralConsts.VIEW_2048_APP_NAME);
		apps.add(GeneralConsts.VIEW_MAZE_APP_NAME);
	}
	
	@Override
	public void displayData(final int[][] data) 
	{
		//NO NEED FOR BOARD(CANVAS REFRESH).
	}
	
	@Override
	public void displayScore(String score) 
	{
		//DO NOTHING. GAME HASNT STARTED YET.
	}
	
}
