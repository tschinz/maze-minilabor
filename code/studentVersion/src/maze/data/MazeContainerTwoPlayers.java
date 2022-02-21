package maze.data;

import java.util.Random;


/**
 * A maze container like {@link MazeContainer} but with two player information
 * in it
 * @author Pierre-Andre Mudry
 * @date February 2012
 * @version 1.0
 */
public class MazeContainerTwoPlayers extends MazeContainer {
	boolean hasTwoPlayers = false;

	/**
	 * Creates a specific maze
	 * 
	 * @param x Width
	 * @param y Height
	 * @param mazeID The unique ID of the maze
	 * @param twoPlayers If the maze should generate two players or a single
	 */
	public MazeContainerTwoPlayers(int x, int y, int mazeID) {
		super(x, y, mazeID);
		setInitialPositions(false);
	}

	/**
	 * Generate a fixed maze (always the same)
	 * 
	 * @param x Width
	 * @param y Height
	 */
	public MazeContainerTwoPlayers(int x, int y) {
		super(x, y);
	}

	/**
	 * TODO Random positions in two player network game is not
	 * implemented
	 */
	protected void setInitialPositions(boolean fixed) {		
		/**
		 * First player top left, second player top right and the exit down
		 * in the middle			 */			
		maze[0][0].p1Present = true;							
		maze[(nCellsX - 1) / 2][nCellsY - 1].isExit = true;
		maze[nCellsX - 1][0].p2Present = true;		
	}
}
