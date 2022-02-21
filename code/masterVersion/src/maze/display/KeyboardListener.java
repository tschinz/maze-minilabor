package maze.display;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import maze.MazeGame;
import maze.data.MazeContainer;
import maze.data.MazeUtils.Direction;

/**
 * Links key presses and actions
 * 
 * @author Pierre-Andre Mudry
 * @date February 2012
 * @version 1.0
 */
public class KeyboardListener implements KeyListener {

	MazeGame mg;
	MazeContainer mc;

	/**
	 * To link keys to actions from the game in the maze, we need references to
	 * both
	 * 
	 * @param mc The maze
	 * @param mg The game
	 */
	public KeyboardListener(MazeContainer mc, MazeGame mg) {
		this.mc = mc;
		this.mg = mg;
	}	

	/**
	 * What happens when a key has been pressed
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {

		/**
		 * Keys for player 1
		 */
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_W:
			mg.movePlayer(Direction.UP);						
			break;
		case KeyEvent.VK_S:
			mg.movePlayer(Direction.DOWN);			
			break;
		case KeyEvent.VK_D:
			mg.movePlayer(Direction.RIGHT);			
			break;
		case KeyEvent.VK_A:
			mg.movePlayer(Direction.LEFT);			
			break;
		case KeyEvent.VK_Q:
			mg.displaySolution();
			break;
		case KeyEvent.VK_N:
			mg.generateNewMaze(new Random().nextInt()); 
			break;
		}		
	}

	/**
	 * This method is called when a key has been released (i.e. no more pressed)
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	/**
	 * This method is called when a key has been pressed and released (complete
	 * cycle)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {

	}
}
