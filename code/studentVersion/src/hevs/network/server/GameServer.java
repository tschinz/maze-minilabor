package hevs.network.server;

import hevs.network.Globals;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class GameServer {
	
	private ServerSocket	_gameServer;
	private int				_serverState;
	private boolean			_isRunning;
	private PatchPannel		_patchPan;
	private int				_randomID;

	private Random rnd = new Random(1);
		
	public GameServer() {
		super();
		
		// Initializations
		_serverState = Globals.STATE_SERVER_DOWN;
		_isRunning = false;
		_patchPan = new PatchPannel();
		_randomID = rnd.nextInt();
		
		// Opening a server socket
		try {
			_gameServer = new ServerSocket( Globals.GAME_SERVER_PORT );
			_serverState = Globals.STATE_SERVER_OPEN;
			_isRunning = true;
			
			// DEBUG
			System.out.println( "[GameServer] GameServer() > Server's address : " + _gameServer.getInetAddress().getHostName() );
			System.out.println( "[GameServer] GameServer() > Server is listening on port " + String.valueOf( _gameServer.getLocalPort() ) );
			
			// check the server must still work
			while( _isRunning ) {
				//Update server's state
				_serverState = Globals.STATE_SERVER_LISTENING;
			
				System.out.println( "[GameServer] run() > Waiting for connections." );
				Socket newClient = _gameServer.accept();
				new Connection( newClient, this );
			}				
		} catch (IOException e) {
			_serverState = Globals.STATE_SERVER_ERROR;
			System.out.println( "[GameServer] GameServer() > IOException : " + e.getMessage() );
			_isRunning = false;
		} finally {
			// Close all connections
			try {
				_gameServer.close();
				_serverState = Globals.STATE_SERVER_DOWN;
			} catch( IOException ioe ) {
				System.out.println( "[GameServer] run() > Closing server : IOException = " + ioe.getMessage() );
			}
		}
	}
	
	synchronized public void addClient( Socket socket, PrintWriter pw ) { _patchPan.subscribe( socket, pw, _randomID ); _randomID = rnd.nextInt();}
	synchronized public void delClient( Socket socket ) { _patchPan.unsubscribe( socket ); }
	synchronized public int getNbClients() { return _patchPan.getNbClients(); }
	synchronized public void receivedData( Socket client, String msg ) { _patchPan.receivedData( client, msg ); }
	
	public int getServerState() { return _serverState; }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GameServer();
	}

	
}
