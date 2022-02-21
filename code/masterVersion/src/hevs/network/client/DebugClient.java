package hevs.network.client;

import maze.data.MazeUtils.Direction;
import maze.data.MazeUtils.Player;
import hevs.network.MazeNetworkInterface;


public class DebugClient extends GameClient implements MazeNetworkInterface {
	private boolean		_isConnected;
	private int			_counter;

	public DebugClient() {
		super();
		_isConnected = false;
		_counter = 0;
	}
	
	public boolean isAssignedToAnotherPlayer() { return _isConnected; }
	
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		DebugClient client = null;
		boolean sender = true;
		
		System.out.println( "[DebugClient] main() : Creating a new game." );
		client = new DebugClient();
		
		System.out.print( "[DebugClient] > Wainting for partner ... " );
		while( !client.isAssignedToAnotherPlayer() )
			Thread.sleep( 500 );
		
		if( sender ) {
//			System.out.print( "[DebugClient] > Type 'Go' and hit return to send some values : " );
//			String in = hevs.utils.Input.readString();
			
			Direction d = Direction.DOWN;
			Player    p = Player.PLAYER1;
			int counter = 0;
			while( client.isAssignedToAnotherPlayer() ) {
				System.out.println( "[DebugClient] sendData : d=" + d.toString() + ";p=" + p.toString() );
				client.notifyMoved( d, p );
				counter++;
				Thread.sleep( 1000 );

				if( counter > 20 ) {
					break;
				}
			}
			
		} else {
			while( client.isAssignedToAnotherPlayer() ) {
				Thread.sleep( 3000 );
				client.notifyMoved( Direction.LEFT, Player.PLAYER2 );
			}
		}
		System.out.println( "[DebugClient] main() : Shutting down ...." );
		System.exit(0);
	}

	@Override
	public void otherPlayerHasMoved( Direction d,  Player p) {
		System.out.println( "[DebugClient] otherPlayerHasMoved : d=" + d.toString() + ";P=" + p.toString() );
		_counter++;
		if( _counter > 9 )
			notifyLeaving();
	}

	@Override
	public void otherPlayerLeft() {
		System.out.println( "[DebugClient] otherPlayerLeft !!!" );
		notifyLeaving();
		_isConnected = false;
	}

	@Override
	public void otherPlayerIncoming(String to, Player yourAreThisPlayer) {
		System.out.println( "[DebugClient] otherPlayerWantsToPlay : " + to );
		_isConnected = true;
	}

}
