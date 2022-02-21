package hevs.network.server;

import hevs.network.Globals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection extends Thread {

	private Socket 			_socket;
	private BufferedReader	_in;
	private PrintWriter		_out;
	private GameServer		_server;

	
	/* ********************************************************************* */
	/*                                                                       */
	/* Constructors                                                          */
	/*                                                                       */
	/* ********************************************************************* */
	
	public Connection() {
		_socket = null;
		_in  = null;
		_server = null;
	}
	
	public Connection( Socket client, GameServer server ) {
		this();
		
		_socket = client;
		_server = server;

//		System.out.println( "[Connection] run() > Accepted Cient : " + _socket.getInetAddress().getHostAddress() );
		System.out.println( "[Connection] run() > A new player is coming in : " + _socket.getInetAddress().getHostAddress() );
		this.start();
	}
	
    
	/* ********************************************************************* */
	/*                                                                       */
	/* Public methods                                                        */
	/*                                                                       */
	/* ********************************************************************* */
	/**
	 * Implementatin of the Runnable interface for Thread class
	 */
	public void run() {
		try {	
			//Get the inputStream
			_in  = Globals.createReaderBuffer( _socket );
			_out = Globals.createWriterBuffer( _socket );
			
			// Subscribe to the patchPannel
//			System.out.println( "[Connection] run() > add Client from server's list ... " );
			_server.addClient( _socket, _out );

		} catch( IOException ioe ) {
			System.out.println( "[Connection] run() > IOException : " + ioe.getMessage() );
			System.exit( -1 );
		}
		
		//Creates receiver's buffer
		String dataIn = null;
		try {		
			// Run in a loop until _in.readLine() is null
			while( (dataIn = _in.readLine()) != null ) {
//				System.out.println( "[Connection] run() > data in : " + dataIn );
				_server.receivedData( _socket, dataIn );
			}
		} catch( IOException ioe ) {
			System.out.println( "[Connection] run() while > IOException : " + ioe.getMessage() );
		}
		
		//Unsubscribe to the patch pannel
//		System.out.println( "[Connection] run() > delete Client from server's list ... " );
		_server.delClient( _socket );
			
//		System.out.println( "[Connection] run() > Closing connection ... " );
		closeSocket();

		// Some housekeeping...
		_in = null;
		_socket = null;
	}

	private void closeSocket () {
		if( _socket != null ) {
			try {
				_socket.close();
			} catch( IOException e ) {
				System.out.print( "[Globals] closeSocket() > IOException : " + e.getMessage() );
			}
		}
	}
	
}
