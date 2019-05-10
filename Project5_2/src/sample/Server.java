package sample;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public class Server extends NetworkConnection {

	private int port;
	
	public Server(int port, Consumer<Serializable> callback) {
		super(callback,true);
		// TODO Auto-generated constructor stub
		this.port = port;
		if(checkPlayers())
		{
			Game();
		}
		//isServer =true;
	}
	//public
	public boolean checkPlayers()
	{
		if(threads.size()==2 && threads.get(0).move != "NAN" && threads.get(0).move != "NAN")
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public List<ClientThread> getThreads()
    {
        return threads;
    }
	public void Game()
	{
		String player1 = threads.get(0).move;
		String player2 = threads.get(1).move;
		if(threads.get(0).move == threads.get(1).move)
		{
			getCallback().accept("Tie");
		}
		if(player1 == "Scissor")
		{
			if(player2 == "Paper" || player2 == "Lizard")
			{
				getCallback().accept("Player 1 wins");
			}
			if(player2 == "Rock" || player2 == "Spock");
			{
				getCallback().accept("Player 2 wins");
			}


		}
		if(player1 == "Paper")
		{
			if(player2 == "Rock" || player2 == "Spock")
			{
				getCallback().accept("Player 1 wins");
			}
			else
			{
				getCallback().accept("Player 2 wins");
			}

		}
		if(player1 == "Rock")
		{
			if(player2 == "Scissor" || player2 == "Lizard")
			{
				getCallback().accept("Player 1 wins");
			}
			else
			{
				getCallback().accept("Player 2 wins");
			}
		}
		if(player1 == "Spock")
		{
			if(player2 == "Scissor" || player2 == "Rock")
			{
				getCallback().accept("Player 1 wins");
			}
			else
			{
				getCallback().accept("Player 2 wins");
			}
		}
		if(player1 == "Lizard")
		{
			if(player2 == "Paper" || player2 == "Spock")
			{
				getCallback().accept("Player 1 wins");
			}
			else
			{
				getCallback().accept("Player 2 wins");
			}
		}

	}
	@Override
	protected boolean isServer() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected String getIP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getPort() {
		// TODO Auto-generated method stub
		return port;
	}

}
