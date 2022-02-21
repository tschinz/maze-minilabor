package hevs.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import maze.data.MazeUtils.Direction;
import maze.data.MazeUtils.Player;

public class Globals {
	
	public static final int GAME_SERVER_PORT		= 55555;
	public static String GAME_SEVER_IP_ADDR	= "192.168.1.197";
	//public static final String GAME_SEVER_IP_ADDR	= "127.0.0.1";
	public static final int READER_BUFFER_SIZE		= 1024;
	public static final boolean WRITER_BUFFER_AUTO_FLUSH = true;

	public static final int STATE_SERVER_ERROR		= -1;
	public static final int STATE_SERVER_DOWN		= 0;
	public static final int STATE_SERVER_OPEN		= 1;
	public static final int STATE_SERVER_LISTENING	= 2;
	
	public static final String MAZE_DIRECTION		= "d:";
	public static final String MAZE_PLAYER			= "p:";
	public static final String MAZE_INFO_SEP		= ";";
	public static final String MAZE_ID				= "MazeID:";
	public static final String CLIENT_CONNECTED		= "Connected:";
	public static final String CLIENT_ID			= "YoueAre:";
	public static final String CLIENT_DECONNECTED	= "OtherPlayerLeft";
	
	public static final int MAZE_DIRECTION_IDX		= 0;
	public static final int MAZE_PLAYER_IDX			= 1;
	

	/**
	 * This method extract the positions x and y from a string coming from the
	 * game server. 
	 * @param position The String to decode
	 * @return An array of 2 bytes containing the X position at index 0 and the
	 *         Y position at the index 1
	 */
	public static String[] decodePosition( String position ) {
		String[] pos = new String[2];
		
		if( position != null && position.length() > 6 ) {
			pos[MAZE_DIRECTION_IDX] = position.substring( position.indexOf( MAZE_DIRECTION )+2, position.indexOf( MAZE_INFO_SEP ) );
			pos[MAZE_PLAYER_IDX] = position.substring( position.indexOf( MAZE_PLAYER )+2 );
		} else
			return null;
		
		return pos;
	}
	
	/**
	 * THis method put the X and Y position into a String to be transmitted
	 * to the gamer server.  
	 * @param posX
	 * @param posY
	 * @return A String with both X and Y positions.
	 */
	public static String encodePosition( Direction d, Player p) {
		return new String( 	MAZE_DIRECTION + d.toString() + MAZE_INFO_SEP + 
							MAZE_PLAYER + p.toString() );		
	}
	
	public static Direction extractDirection( String direction ) {
		if( direction.compareToIgnoreCase( Direction.DOWN.toString() ) == 0 )
			return Direction.DOWN;
		else if( direction.compareToIgnoreCase( Direction.UP.toString() ) == 0 )
			return Direction.UP;
		else if( direction.compareToIgnoreCase( Direction.LEFT.toString() ) == 0 )
			return Direction.LEFT;
		else
			return Direction.RIGHT;
	}

	public static Player extractPlayer( String player ) {
		if( player.compareToIgnoreCase( Player.PLAYER1.toString() ) == 0 )
			return Player.PLAYER1;
		else
			return Player.PLAYER2;
	}

	public static boolean sendUTF( ObjectOutputStream oos, String msg ) {
		try {
			oos.writeUTF( msg );
			oos.flush();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static BufferedReader createReaderBuffer( Socket socket ) throws IOException {
		return new BufferedReader( new InputStreamReader( socket.getInputStream() ), READER_BUFFER_SIZE );
	}
	
	public static PrintWriter createWriterBuffer( Socket socket ) throws IOException {
		return new PrintWriter( new OutputStreamWriter( socket.getOutputStream() ), WRITER_BUFFER_AUTO_FLUSH );
	}
}
