package mvp.consts;

public class CommandsConsts {
	
	/*
	 * VIEW COMMANDS
	 */
	
		/*
		 *VIEW MOVEMENT COMMANDS - START
		 */
		
		public static final int VIEW_MOVE_UP = 1;// - SHARED maze,2048
		public static final int VIEW_MOVE_DOWN = 2;// - SHARED maze,2048
		public static final int VIEW_MOVE_LEFT = 3;// - SHARED maze,2048
		public static final int VIEW_MOVE_RIGHT = 4;// - SHARED maze,2048
		public static final int VIEW_MOVE_UP_LEFT = 5;//maze
		public static final int VIEW_MOVE_UP_RIGHT = 6;//maze
		public static final int VIEW_MOVE_DOWN_LEFT = 7;//maze
		public static final int VIEW_MOVE_DOWN_RIGHT = 8;//maze
		public static final int VIEW_MOVE_UNDO = 9;//maze
		public static final int VIEW_MOVEMENT_MAX_COMMAND = 50;//SHARED all<Presenter>
		
		/*
		 *VIEW MOVEMENT COMMANDS - END
		 */
		
		/*
		 * VIEW TECHNICAL COMMANDS - START
		 */
		public static final int VIEW_TEC_INITIATE_BOARD = 51;//SHARED maze,2048
		public static final int VIEW_TEC_RESTART_BOARD = 52;//SHARED maze,2048
		public static final int VIEW_TEC_LOAD_BOARD = 53;//SHARED maze,2048
		public static final int VIEW_TEC_SAVE_BOARD = 54;//SHARED maze,2048
		
		public static final int VIEW_TEC_BOARD_PREVIEW = 76;//maze
		
		public static final int VIEW_TEC_IS_LOST_GAME = 98;//SHARED maze,2048
		public static final int VIEW_TEC_IS_WON_GAME = 99;//SHARED maze,2048
		
		public static final int VIEW_TEC_MAX_COMMAND = 100;//SHARED all<Presenter>
		
		/*
		 * VIEW TECHNICAL COMMANDS - END
		 */
		
		/*
		 * MODEL SHOW COMMANDS - START
		 */
		public static final int MODEL_SHOW_GENERATED_MAZE = 101; //maze
		public static final int MODEL_SHOW_WON_GAME = 102;//maze, 2048
		public static final int MODEL_SHOW_LOST_GAME = 103; // 2048
		
		/*
		 * MODEL SHOW COMMANDS - START
		 */
		
		
}
