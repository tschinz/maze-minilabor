package hevs.network.server;

import hevs.network.Globals;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import maze.data.MazeUtils.Player;

public class PatchPannel {
	
	private Vector<Socket> 		_clients;
	private Vector<PrintWriter> _pws;
	private int					_nbClients;
	
	
	/* ********************************************************************* */
	/*                                                                       */
	/* Constructors                                                          */
	/*                                                                       */
	/* ********************************************************************* */
	public PatchPannel() {
		_nbClients = 0;
		_clients = new Vector<Socket>();
		_pws     = new Vector<PrintWriter>();
		System.out.println( "[PatchPannel] Constructor > _nbClients = " + String.valueOf( _nbClients ) );
	}


	/* ********************************************************************* */
	/*                                                                       */
	/* Public methods                                                        */
	/*                                                                       */
	/* ********************************************************************* */
	/**
	 * Register a new client connected to the game server 
	 * @param client The just connected gamer !
	 */
	synchronized public void subscribe( Socket client, PrintWriter pw, int randomId ) {
		if( client != null ) {
			_nbClients++;					// Increment the number of connected clients !
			_clients.addElement( client );	// Add the socket of this new client
			_pws.addElement( pw );
			System.out.println( "[PatchPannel] subscribe() > _nbClients : " + String.valueOf( _nbClients ) );
			notifyClients( _nbClients, randomId );
		}		
	}
	
	/**
	 * Unregister an connected client. 
	 * @param client
	 */
	synchronized public void unsubscribe( Socket client ) {
		if( client != null && _clients.contains( client ) ) {
			int index = _clients.indexOf( client );

			//Handling the other connected clients
			int peerIdx = getThePair( index );
			if( peerIdx != -1  && peerIdx < _pws.size() ) {
				//Notify the other player
				_pws.get( peerIdx ).println( Globals.CLIENT_DECONNECTED );
			}

			//Handling current connected client
			_clients.remove( client );
			_pws.remove( index );
			_nbClients--;
			System.out.println( "[PatchPannel] unsubscribe() 'client' > _nbClients : " + String.valueOf( _nbClients ) );

		}	
	}
	
	synchronized public void receivedData( Socket client, String msg ) {
		if( client != null && _clients.contains( client ) ) {
			int index = getThePair( _clients.indexOf( client ) );
			
			System.out.println( "[PatchPannel] receivedData() > index : " + String.valueOf( index ) );
			if( index != -1 && index < _pws.size() ) {
				send( _pws.get( index ), msg );
			}
		}
	}
	
	synchronized public int getNbClients() {
		return _nbClients;
	}
	
	
	/* ********************************************************************* */
	/*                                                                       */
	/* Private methods                                                       */
	/*                                                                       */
	/* ********************************************************************* */
	private int getThePair( int index ) {
		if( index % 2 == 0 )
			return index + 1;
		else if( index > 0)
			return index - 1;
		else
			return -1;
	}
	
	private void notifyClients( int nbOfConnectedClient, int mazeID ) {
		if( nbOfConnectedClient % 2 == 0 ) {
			Socket peer = _clients.elementAt( nbOfConnectedClient - 1 );
			Socket user = _clients.elementAt( nbOfConnectedClient - 2 );
			
			System.out.println( "[PatchPannel] notifyClient : " + Globals.CLIENT_CONNECTED + user.getInetAddress().getHostAddress() + Globals.MAZE_INFO_SEP + Globals.CLIENT_ID + Player.PLAYER2.toString() + Globals.MAZE_INFO_SEP + Globals.MAZE_ID + String.valueOf( mazeID ) );
			send( getPrintWriter( peer ), Globals.CLIENT_CONNECTED + user.getInetAddress().getHostAddress() + Globals.MAZE_INFO_SEP + Globals.CLIENT_ID + Player.PLAYER2.toString() + Globals.MAZE_INFO_SEP + Globals.MAZE_ID + String.valueOf( mazeID ) );
			System.out.println( "[PatchPannel] notifyClient : " + Globals.CLIENT_CONNECTED + user.getInetAddress().getHostAddress() + Globals.MAZE_INFO_SEP + Globals.CLIENT_ID + Player.PLAYER1.toString() + Globals.MAZE_INFO_SEP + Globals.MAZE_ID + String.valueOf( mazeID ) );
			send( getPrintWriter( user ), Globals.CLIENT_CONNECTED + peer.getInetAddress().getHostAddress() + Globals.MAZE_INFO_SEP + Globals.CLIENT_ID + Player.PLAYER1.toString() + Globals.MAZE_INFO_SEP + Globals.MAZE_ID + String.valueOf( mazeID ) );			
		}		
	}
	
	private PrintWriter getPrintWriter( Socket socket ) {
		PrintWriter out;
		try {
			out = Globals.createWriterBuffer( socket );
		} catch( IOException e ) {
			System.out.print( "[PatchPannel] getPrintWriter() > IOException : " + e.getMessage() );
			out = null;
		}
		return out;
	}
	
	private void send( PrintWriter pw, String message ) {
		pw.println( message );
		pw.flush();
	}
}
