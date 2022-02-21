package maze.display;

import maze.data.MazeContainer;
import maze.data.MazeElem;

/**
 * A class that displays a textual version of the maze given in the form of a
 * {@link MazeContainer}
 * 
 * @author Put your name here
 * @date February 29th, 2012
 * @version 1.0
 */
public class TextDisplay_BEGIN {

	/**
	 * Displays the maze given in parameter on the default console
	 * 
	 * @param mazeC The {@link MazeContainer} to display
	 */
	public static void displayMaze(MazeContainer mazeC) {

		// Get the real labyrinth
		MazeElem[][] maze = mazeC.maze;

		// Size of the labyrinth
		int nCellsX = mazeC.nCellsX;
		int nCellsY = mazeC.nCellsY;

		/**
		 * Draw the labyrinth
		 */
		for (int i = 0; i < nCellsY; i++) {
			// Draws the north edge
			for (int j = 0; j < nCellsX; j++) {
				MazeElem e = maze[j][i];

				// TODO Task 1

				System.out.print("*   ");
			}

			System.out.println("*");

			// Draws the west edge
			for (int j = 0; j < nCellsX; j++) {
				MazeElem e = maze[j][i];

				// TODO Task 1

				System.out.print("    ");
			}
			System.out.println("|");
		}
		// Draws the bottom line
		for (int j = 0; j < nCellsX; j++) {
			System.out.print("*---");
		}
		System.out.println("*");
	}

	public static void main(String args[]) {
		MazeContainer mg = new MazeContainer(5,5);
		TextDisplay_BEGIN.displayMaze(mg);
	}
}
