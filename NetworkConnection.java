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
			String dataString = "";
			
			for(ClientInfo client : clients)
			{
				if(!client.hasResponded())
				{
					callback.accept("Player " + client.getID() + " still need to select a sentence.");
				}
				else
				{
					dataString += "Player " + client.getID() + ": " + client.getResponse() + NEWLINE;
					responsesReady++;
				}
			}
			
			if(responsesReady > 0)
			{
				if(responsesReady == clients.size()) //every client has submitted a response
				{	
						
					for(ClientInfo client : clients)
					{
						try {
							client.sendData(dataString);
							client.resetRound(); //clear opponents
							client.clearResponse();
								
						} catch (IOException e) {}
					}
					callback.accept("Enter # of best response (sentences in random order).\n" + dataString);
				}
			}
			else
				callback.accept("No responses available.");
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
						getClientByID(id).sendData("Your Deck");
					}
					else if(Game.matchCommand(dataString, GameCommands.CLIENT_GET_SCENARIO))
					{
						getClientByID(id).sendData("Your Scenarios");
					}
					else if(Game.matchCommand(dataString, GameCommands.CLIENT_RESPONSE))
					{
						String clientStringResponse = dataString.replace(GameCommands.CLIENT_RESPONSE.toString(), "");
						getClientByID(id).setResponse(clientStringResponse);
						
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
		
		public void run() {
			
			if(isServer())
			{
				try(ServerSocket server = new ServerSocket(getPort())) {
					while(getNumClients() < MAX_PLAYERS)
					{
						ClientInfo client = new ClientInfo(new ClientThread(server.accept(), ++playerCount));
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

