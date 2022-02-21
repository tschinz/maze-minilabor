package hevs.network;

import maze.data.MazeUtils.Direction;
import maze.data.MazeUtils.Player;

public interface MazeNetworkInterface {
	/**
	 * Connections to server callbacks
	 */
	// TODO would be nice to have
	//	public void connectedToServer();	
	//	public void connectionToServerLost();
	
	/**
	 * Other player movements	 
	 **/
	public void otherPlayerHasMoved( Direction d, Player p );
	public void otherPlayerIncoming( String serverName ,  Player yourAreThisPlayer , int mazeID );
	public void otherPlayerLeft();
}
