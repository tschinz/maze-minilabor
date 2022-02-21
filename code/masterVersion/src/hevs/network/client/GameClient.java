package hevs.network.client;

import hevs.network.Globals;
import hevs.network.MazeNetworkInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import maze.data.MazeUtils.Direction;
import maze.data.MazeUtils.Player;

public abstract class GameClient extends Thread implements MazeNetworkInterface {
	private BufferedReader	_in;			// InputStream for the connected socket 
	private PrintWriter		_out;			// Output Stream for current socket
	private Socket			_socket;		// Socket to be use to connect to the game server

	/* ********************************************************************* */
	/*                                                                       */
	/* Constructors                                                          */
	/*                                                                       */
	/* ********************************************************************* */
	public GameClient() {
		// init
		_in  = null;
		_out = null;
		_socket = null;
		
		// Try to connect to the server
		try {
			System.out.print( "[GameClient] Constructor > Connecting to the game server... " );
			_socket = new Socket( Globals.GAME_SEVER_IP_ADDR, Globals.GAME_SERVER_PORT );
			System.out.println( "[Ok]" );
			
			this.start();
		} catch( UnknownHostException uhe ) {
			System.out.println( "[Failed] UnknownHostException : " + uhe.getMessage() );
		} catch( IOException ioe ) {
			System.out.println( "[Failed] IOException : " + ioe.getMessage() );
		}
	}
	
    
	/* ********************************************************************* */
	/*                                                                       */
	/* Protected Methods                                                     */
	/*                                                                       */
	/* ********************************************************************* */
	
	public boolean notifyMoved( Direction d, Player p ) {
		if( _out != null ) {
			//Send the new position to the server
			System.out.println( "[GameClient] notifyMoved(int, int) > Sending new position to the server... " );
			_out.println( Globals.encodePosition( d, p ) );
			_out.flush();
			return true;
		}
		return false;
	}
	
	public void notifyLeaving() {
		//Closing the socket
		closeSocket();
	}

	
	/* ********************************************************************* */
	/*                                                                       */
	/* Public methods                                                        */
	/*                                                                       */
	/* ********************************************************************* */
	
	/**
	 * Implementation of the Runnable interface for Thread class
	 */
	public void run() {
		try {
			System.out.print( "[GameClient] run() > Opening the streams... " );
			_in = new BufferedReader(new InputStreamReader( _socket.getInputStream() ) );
			_out = new PrintWriter(new OutputStreamWriter( _socket.getOutputStream() ) );
			System.out.println( "[Ok]" );
		} catch( IOException ioe ) {
			System.out.println( "[Failed] IOException : " + ioe.getMessage() );
			System.exit( -1 );
		}
				
		//Creates receiver's buffer
		String dataIn = null;
		try {		
			// Run in a loop until _running is set to false
			while( (dataIn = _in.readLine()) != null ) {
				System.out.println( "[GameClient] run() > data in : " + dataIn );
				analyzeData( dataIn );
			}
		} catch( IOException ioe ) {
			System.out.println( "[GameClient] run() > IOException : " + ioe.getMessage() );
		}

		//Closing the socket
		System.out.println( "[GameClient] Closing active socket... " );
		closeSocket();

		// Notify the peer client
		otherPlayerLeft();
	
		_in = null;
		_out = null;
		_socket = null;
	}
	
	
	/* ************************************************************************************************************ */
	/*                                                                                                              */
	/* Private methods                                                                                              */
	/*                                                                                                              */
	/* ************************************************************************************************************ */
	
	private void analyzeData( String data ) {
		//Analyze Connected: message
		if( data.startsWith( Globals.CLIENT_CONNECTED ) ) {
			//Decode message
			String ipAddr = data.substring( Globals.CLIENT_CONNECTED.length() );
			System.out.println( "[GameClient] analyzeData > inetAddress : " + ipAddr );
			
			//Decode player
			Player player = Globals.extractPlayer( data.substring( data.indexOf( Globals.CLIENT_ID) + Globals.CLIENT_ID.length(), 
																   data.lastIndexOf( Globals.MAZE_INFO_SEP ) ) );

			//Decode Maze ID =
			int rand = Integer.parseInt( data.substring( data.indexOf( Globals.MAZE_ID) + Globals.MAZE_ID.length() ) );
			
			//Notify client
			System.out.println( "[GameClient] analyzeData > Notify the connection between 2 player ... " );
			otherPlayerIncoming( ipAddr, player, rand );
			
		// Other client left	
		} else if( data.startsWith( Globals.CLIENT_DECONNECTED ) ) {
			//Notify client
			System.out.println( "[GameClient] analyzeData > Notify client that other play left ... " );
			otherPlayerLeft();

		//Analyze the X & Y positions
		} else if( data.startsWith( Globals.MAZE_DIRECTION ) ) {
			//Decoding the X & Y positions
			String[] newPos = Globals.decodePosition( data );
			System.out.println( "[GameClient] analyzeData > decoded Position : X=" + 
								String.valueOf( newPos[ Globals.MAZE_DIRECTION_IDX ] ) + ";Y=" + 
								String.valueOf( newPos[ Globals.MAZE_PLAYER_IDX ] ) );
			
			//Notify the client
			otherPlayerHasMoved( Globals.extractDirection( newPos[ Globals.MAZE_DIRECTION_IDX ] ), 
								 Globals.extractPlayer( newPos[ Globals.MAZE_PLAYER_IDX ] ) );
		}
	}
	
	private void closeSocket() {
		if( _socket != null ) {
			try {
				_socket.close();				
			} catch( IOException e ) {
				System.out.print( "[GameClient] closeSocket() > IOException : " + e.getMessage() );
			}
		}
	}
	
}
