package projectFive;

import java.io.IOException;
import java.util.ArrayList;

import projectFive.Game.Card;
import projectFive.NetworkConnection.ClientThread;

public class ClientInfo {
	private int id;
	private int points = 0;
	private int opponentID = 0;
	private ClientThread thread;
	private String response = null;
	private static ArrayList<String> deck;
	
	ClientInfo(ClientThread thread)
	{
		this.thread = thread;
		id = this.thread.getID();
	}
	
	public void startThread()
	{
		thread.start();
	}
	
	public boolean isBusy()
	{
		return opponentID > 0;
	}
	
	public void startRound(int opponentID)
	{
		this.opponentID = opponentID;
	}
	
	public void resetRound()
	{
		this.opponentID = 0;
	}
	
	public void addPoint()
	{
		points++;
	}
	
	public int getID()
	{
		return id;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public boolean hasResponded()
	{
		return response != null;
	}
	
	public String getResponse()
	{
		return response;
	}
	
	public void clearResponse()
	{
		response = null;
	}
	
	public void addCardToDeck(String card)
	{
		deck.add(card);
	}
	
	public ArrayList<String> getDeck()
	{
		return deck;
	}
	
	public void setResponse(String sentence)
	{
		this.response = sentence;
		
	}
	
	public void sendData(Object data) throws IOException
	{
		thread.out.writeObject(data);
	}
}