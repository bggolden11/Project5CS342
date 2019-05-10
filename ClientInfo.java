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
	private int currentCardID = -1;
	private ArrayList<String> deck;
	
	ClientInfo(ClientThread thread)
	{
		this.thread = thread;
		id = this.thread.getID();
		deck = new ArrayList<String>();
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
		if(currentCardID != -1)
			getDeck().remove(currentCardID);
		
		response = null;
	}
	
	public void addCardToDeck(String card)
	{
		deck.add(card);
	}
	
	public String readDeck()
	{
		String str = "";
		for(int i = 0; i < deck.size(); i++)
		{
			str += (i + 1) + ") " + deck.get(i) + "\n";
		}
		return str;
	}
	
	public ArrayList<String> getDeck()
	{
		return deck;
	}
	
	public void setResponse(int deckID)
	{
		currentCardID = deckID;
		this.response = getDeck().get(deckID);
		
	}
	
	public void sendData(Object data) throws IOException
	{
		thread.out.writeObject(data);
	}
}