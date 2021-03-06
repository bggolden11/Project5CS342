package projectFive;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import projectFive.Game;
import projectFive.Game.GameCommands;
import javafx.application.Platform;

public abstract class NetworkConnection {
	
	private ConnThread connthread;
	private Consumer<Serializable> callback;

	ArrayList<ClientInfo> clients;
	
	private int playerCount = 0;
	int localID = 0;
	
	private String currentScenario = null;
	
	private final int MAX_PLAYERS = 8;
	
	private final String NEWLINE = "\n";
	private final String DBLNEWLINE = "\n\n";

	private FXNet ui;
	
	public NetworkConnection(FXNet ui, Consumer<Serializable> callback) {
		this.ui = ui;
		this.callback = callback;
		connthread = new ConnThread();
		connthread.setDaemon(true);
		
		clients = new ArrayList<ClientInfo>();
	}
	
	public int getClientCounter()
	{
		return playerCount;
	}
	
	public int getNumClients()
	{
		return clients.size();
	}
	
	public void setLocalID(int id)
	{
		localID = id;
		ui.assignClient();
	}
	
	public void setScenario(String str)
	{
		currentScenario = str;
	}
	
	public String getScenario()
	{
		return currentScenario;
	}
	
	public void clearScenario()
	{
		currentScenario = null;
	}
	
	public int getLocalID()
	{		
		return localID; //server
	}
	
	public ClientInfo getClientByID(int id)
	{
		for(ClientInfo client : clients)
		{
			if(client.getID() == id)
				return client;
		}
		
		return null;
	}
	
	public void startConn() throws Exception{

		connthread.start();
	}
	
	public void send(Serializable data) throws Exception {
		if(isServer())
		{
			int responsesReady = 0;
			String dataString = data.toString().trim();
			
			for(ClientInfo client : clients)
			{
				if(!client.hasResponded())
				{
					callback.accept("Player(s) still need to select a sentence.");
				}
				else
				{
					if(Game.matchCommand(dataString, GameCommands.DECK_PLAYANSWERS))
						callback.accept(client.getID() + ") " + client.getResponse());	
					responsesReady++;
				}
					
			}
			
			if(Game.matchCommand(dataString, GameCommands.CLIENT_AWARD) && responsesReady > 0)
			{
				if(responsesReady == clients.size()) //every client has submitted a response
				{
					int response = Integer.parseInt(dataString.replace(GameCommands.CLIENT_AWARD.toString(), ""));
					
					dataString = "Selected Winner: " + getClientByID(response).getResponse();
					
					getClientByID(response).addPoint();
						
					for(ClientInfo client : clients)
					{
						try {
							if(client.getID() != response)
								client.sendData("You lost");
							else
								client.sendData("You Won!");
								
							client.sendData(dataString + "\nYou have " + client.getPoints() + " points.\n");
							client.clearResponse();
								
						} catch (IOException e) {
						}
					}
				
					callback.accept(dataString);
					
					clearScenario();
				}
			}
			else if(!Game.matchCommand(dataString, GameCommands.DECK_PLAYANSWERS))
				callback.accept("Need more responses.");
		}
		else
			connthread.out.writeObject(data);
	}
	
	public void closeConn() throws Exception{
		if(connthread.socket != null)
			connthread.socket.close();
	}
	
	abstract protected boolean isServer();
	abstract protected String getIP();
	abstract protected int getPort();
	
	class ClientThread extends Thread{

		private Socket socket;
		private int id;
		ObjectOutputStream out;
		ObjectInputStream in;
		
		ClientThread(Socket socket, int id)
		{
			this.socket = socket;
			this.id = id;
		}
		
		public int getID()
		{
			return id;
		}
		
		public void run() {
			try{
				this.out = new ObjectOutputStream(socket.getOutputStream());
				this.in = new ObjectInputStream(socket.getInputStream());
			
				socket.setTcpNoDelay(true);
				
				while(true) { //Incoming data from client
					Serializable data = (Serializable) in.readObject();
					
					String dataString = data.toString().trim();
					
					if(Game.matchCommand(dataString, GameCommands.CLIENT_WHOAMI))
					{
						getClientByID(id).sendData(GameCommands.CLIENT_ASSIGN.toString() + id);
						getClientByID(id).sendData("You are Player " + id);
					}
					else if(Game.matchCommand(dataString, GameCommands.CLIENT_LOBBY))
					{
						String lobby = "List of Players: ";
						for(ClientInfo client : clients)
						{
							int c = client.getID();
							
							if(id != c)
								lobby += "Player " + c + "  ";
						}
						getClientByID(id).sendData(lobby);
					}
					else if(Game.matchCommand(dataString, GameCommands.CLIENT_GET_ANSWER_OPT))
					{	
						getClientByID(id).sendData(getClientByID(id).readDeck());
					}
					else if(Game.matchCommand(dataString, GameCommands.CLIENT_GET_SCENARIO))
					{
						if(currentScenario == null)
							getClientByID(id).sendData("Server has not selected a scenario");
						else
							getClientByID(id).sendData(currentScenario);
					}
					else if(Game.matchCommand(dataString, GameCommands.CLIENT_RESPONSE))
					{
						String clientStringResponse = dataString.replace(GameCommands.CLIENT_RESPONSE.toString(), "");
						
						if(!Game.isInteger(clientStringResponse))
						{
							getClientByID(id).sendData("Invalid answer.");
						}
						else
						{
							int responseID = Integer.parseInt(clientStringResponse);
							responseID--;
							
							if(responseID <= 0 || responseID < getClientByID(id).getDeck().size())
							{
								getClientByID(id).setResponse(responseID);
							
								int responsesReady = 0;
								for(ClientInfo client : clients)
								{
									if(client.hasResponded())
										responsesReady++;
								}
								
								if(responsesReady == clients.size()) //every client has submitted a response
									callback.accept("Ready to view player selections.");
								
								for(ClientInfo client : clients)
								{
									int c = client.getID();
									
									if(id != c)
										client.sendData("Player " + id + " has chosen a card.");
									else
										client.sendData("Sentence recieved.");
									
									if(responsesReady == clients.size()) //every client has submitted a response
										client.sendData("All responses received, awaiting selection of winner.");
								}
							}
							else
								getClientByID(id).sendData("Invalid answer.");
						}
					}
				}
				
			}
			catch(Exception e) {
				try
				{
					for(ClientInfo client : clients)
					{
						if(client.getID() == id)
						{
							clients.remove(client);
							callback.accept("Player " + id + " has left the game.");
							break;
						}
						else
							client.sendData("Player " + id + " has left the game.");
					}
				}
				catch (Exception e2) //server probably on fire, if this happens blame brian
				{
					e2.printStackTrace();
				}
				
			}
		}
	}
	
	class ConnThread extends Thread{
		
		Socket socket;
		private ObjectOutputStream out;
		Game gameEngine;
		
		public void run() {
			
			if(isServer())
			{
				try(ServerSocket server = new ServerSocket(getPort())) {
					gameEngine = new Game();
					while(getNumClients() < MAX_PLAYERS)
					{
						ClientInfo client = new ClientInfo(new ClientThread(server.accept(), ++playerCount));
						
						for(int i = 0; i < 5; i++)
							client.addCardToDeck(gameEngine.drawDeckRandom(GameCommands.DECK_ANSWERS));
						
						clients.add(client);
						
						client.startThread();	
							
						callback.accept("Player " + client.getID() + " has connected.");
						
						for(ClientInfo c : clients)
						{
							if(c.getID() != playerCount)
								c.sendData("Player " + client.getID() + " has connected.");
						}
					}
					callback.accept("Maximum players, not accepting any more clients.");
				}
				catch(Exception e)
				{
					callback.accept("Client Disconnected.");
				}
			}
			else
			{
				try(Socket socket = new Socket(getIP(), getPort());
						ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream());
						ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
					
					this.socket = socket;
					this.out = out;
					socket.setTcpNoDelay(true);
					
					while(true) {
						Serializable data = (Serializable) in.readObject();
						String dataString = data.toString();
						
						if(Game.matchCommand(dataString, GameCommands.CLIENT_ASSIGN))
						{
							dataString = (data.toString().replace(GameCommands.CLIENT_ASSIGN.toString(), ""));
							if (Game.isInteger(dataString))
								setLocalID(Integer.parseInt(dataString));
						}
						else
							callback.accept(data);
					}
					
				}
				catch(Exception e) {
					callback.accept("Server Disconnected.");
				}
			}
		}
	}
	
}	

