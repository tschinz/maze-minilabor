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
	private PatchPannel		_patchPan;
	private int				_randomID;

	private Random rnd = new Random(1);
		
	public GameServer() {
		super();
		
		// Initializations
		_serverState = Globals.STATE_SERVER_DOWN;
		_patchPan = new PatchPannel();
		_randomID = rnd.nextInt();
		
		// Opening a server socket
		try {
			_gameServer = new ServerSocket( Globals.GAME_SERVER_PORT );
			_serverState = Globals.STATE_SERVER_OPEN;
			
			// DEBUG
			System.out.println( "[GameServer] GameServer() > Server is listening on port " + String.valueOf( _gameServer.getLocalPort() ) );
			
			// check the server must still work
			while( true ) {
				//Update server's state
				_serverState = Globals.STATE_SERVER_LISTENING;
			
				Socket newClient = _gameServer.accept();
				new Connection( newClient, this );
			}				
		} catch (IOException e) {
			_serverState = Globals.STATE_SERVER_ERROR;
			System.out.println( "[GameServer] GameServer() > IOException : " + e.getMessage() );
		} 

		// Close all connections
		try {
			if( _gameServer != null ) _gameServer.close();
			_serverState = Globals.STATE_SERVER_DOWN;
		} catch( IOException ioe ) {
			System.out.println( "[GameServer] run() > Closing server : IOException = " + ioe.getMessage() );
		}
	}
	
	synchronized public void addClient( Socket socket, PrintWriter pw ) { 
		_patchPan.subscribe( socket, pw, _randomID ); 
		_randomID = rnd.nextInt();
	}
	
	synchronized public void delClient( Socket socket ) { _patchPan.unsubscribe( socket ); }
	synchronized public int getNbClients() { return _patchPan.getNbClients(); }
	synchronized public void receivedData( Socket client, String msg ) { _patchPan.receivedData( client, msg ); }
	
	public int getServerState() { return _serverState; }

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		new GameServer();
	}
	
}
