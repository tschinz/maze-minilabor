package maze;

import hevs.network.Globals;
import hevs.network.client.GameClient;

import javax.swing.JOptionPane;

import maze.data.MazeContainer;
import maze.data.MazeContainerTwoPlayers;
import maze.data.MazeElem;
import maze.data.MazeUtils.Direction;
import maze.data.MazeUtils.Player;

/**
 * Multiplayer game for Maze
 * @author Pierre-Andre Mudry
 * @date February 2012
 * @version 1.0
 */
public class MazeNetworkGame_BEGIN extends MazeGame {

	GameClient client = null;
	boolean isConnected = false;

	public MazeNetworkGame_BEGIN(MazeContainer mc) {

		super(mc);

		gd.setMessage("Connecting to server...");

		client = new GameClient() {

			@Override
			public void otherPlayerIncoming(String serverIP, Player yourAreThisPlayer, int mazeID) {							
				
				// TODO Complete here Part3.1
							
				
				/**
				 * Inform the objects what they are
				 */
				setPlayer(yourAreThisPlayer);
				isConnected = true;
			}

			@Override
			public void otherPlayerLeft() {
				String msg = "> Other player left the game";				
				System.out.println(msg);
				
				/**
				 * Display information on screen and console
				 */
				isConnected = false;
				gd.setMessage(msg);
			}

			@Override
			public void otherPlayerHasMoved(Direction d, Player p) {
				
				// TODO Complete here Part 3.3
			}
		};
	}

	/**
	 * Call this when you want a new game
	 * 
	 * @param mazeID
	 */
	@Override
	public void generateNewMaze(int mazeID) {
		this.setNewMaze(new MazeContainerTwoPlayers(width, height, mazeID));
	}

	/**
	 * Chooses which player it is
	 * 
	 * @param p
	 */
	public void setPlayer(Player p) {
		player = p;
	}

	/**
	 * Method used to move a player inside the maze. This method is 
	 * different from the single player game
	 * 
	 * @param d Which direction do you want to move
	 * @param playerF Which player
	 */
	public void movePlayer(Direction d, Player player) {
		for (int j = 0; j < height; j++)
			for (int i = 0; i < width; i++) {

				MazeElem e = maze[i][j];

				if (e.p1Present && player == Player.PLAYER1 || e.p2Present && player == Player.PLAYER2) {

					switch (d) {
					case UP:
						if (!e.wallNorth) {
							if (player == Player.PLAYER1) {
								e.p1Present = false;
								maze[i][j - 1].p1Present = true;
							} else {
								e.p2Present = false;
								maze[i][j - 1].p2Present = true;
							}
							return;
						}
						break;

					case DOWN:
						if (!e.wallSouth) {
							if (player == Player.PLAYER1) {
								e.p1Present = false;
								maze[i][j + 1].p1Present = true;
							} else {
								e.p2Present = false;
								maze[i][j + 1].p2Present = true;
							}

							return;
						}
						break;

					case RIGHT:
						if (!e.wallEast) {
							if (player == Player.PLAYER1) {
								e.p1Present = false;
								maze[i + 1][j].p1Present = true;
							} else {
								e.p2Present = false;
								maze[i + 1][j].p2Present = true;
							}

							return;
						}
						break;

					case LEFT:
						if (!e.wallWest) {
							if (player == Player.PLAYER1) {
								e.p1Present = false;
								maze[i - 1][j].p1Present = true;
							} else {
								e.p2Present = false;
								maze[i - 1][j].p2Present = true;
							}

							return;
						}
						break;
					}
					return;
				}
			}
	}

	/**
	 * This method is called when we move
	 */
	@Override
	public void movePlayer(Direction d) {
		if (!isConnected) {
			/**
			 * If we are not connected, we should not move
			 */
			final String msg = "You are not connected !";
			System.out.println(msg);
			gd.setMessage(msg);
			return;
		} else {			
			/**
			 * When we are connected and we move
			 * alert the other player we moved, displace 
			 * ourself on our board and check if there is a winner
			 */
			client.notifyMoved(d, player);
			movePlayer(d, player);
			checkWinner();
		}
	}

	/**
	 * Check if some player has reached the exit of the maze
	 */
	@Override
	public boolean checkWinner() {
		/**
		 * If we're not connected, this doesn't make sense
		 */
		if (!isConnected) {
			return false;
		}

		boolean winnerFound = false;
		boolean iWon = false;
		
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				MazeElem el = maze[i][j];

				if (el.isExit && el.p1Present) {
					winnerFound = true;					
					if(player == Player.PLAYER1){					
						JOptionPane.showMessageDialog(null, "You won !", "We have a winner !", JOptionPane.INFORMATION_MESSAGE);
						iWon = true;
					}
					else
						JOptionPane.showMessageDialog(null, "You lost !", "We have a winner !", JOptionPane.INFORMATION_MESSAGE);

				}

				if (el.isExit && el.p2Present) {
					winnerFound = true;
					if(player == Player.PLAYER2){
						iWon = true;
						JOptionPane.showMessageDialog(null, "You won !", "We have a winner !", JOptionPane.INFORMATION_MESSAGE);						
					}
					else
						JOptionPane.showMessageDialog(null, "You lost !", "We have a winner !", JOptionPane.INFORMATION_MESSAGE);					
				}
			}
		}

		/**
		 * If a winner has been found, the loser informs the other
		 * player that he quits the game and then exits.
		 */
		if (winnerFound) {
			if(!iWon){				
				client.notifyLeaving();
				System.exit(0);
			}
			
			return true;
		}
				
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		/**
		 * Did someone specified the server address ??
		 */
		if(args.length == 1){
			Globals.GAME_SEVER_IP_ADDR = args[0];			
		}			

		new MazeNetworkGame_BEGIN(new MazeContainerTwoPlayers(10, 10, 1));
			
	}
}
