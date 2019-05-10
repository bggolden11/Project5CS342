package projectFive;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Game {

	private static final String NEWLINE = "\n";
	private static final String DBLNEWLINE = "\n\n";
	
	private static final String GAME_SCENARIO_DICTIONARY = "projectFive/serverScenarios.txt";
	private static final String GAME_ANSWERS_DICTIONARY = "projectFive/serverAnswers.txt";
	
    private static ArrayList<Card> scenarios; //contains scenarios for cards against humanity
    private static ArrayList<Card> answers; //contains all the cards for deck
    
    public Game()
    {
    	scenarios = initDeck(GAME_SCENARIO_DICTIONARY);
    	answers = initDeck(GAME_ANSWERS_DICTIONARY);
    }
	
	/*resource: https://www.baeldung.com/java-check-string-number*/
	public static boolean isInteger(String str) {
	    try {
	        int i = Integer.parseInt(str);
	    } catch (Exception e) {
	        return false;
	    }
	    
	    return true;
	}
	
	public static enum GameCommands
	{
		CLIENT_WHOAMI,
		CLIENT_RESPONSE,
		CLIENT_LOBBY,
		CLIENT_ASSIGN,
		
		DECK_SCENARIO,
		DECK_ANSWERS,
		
		MISC_MESSAGE,
	};
	
	//creates object card
	class Card {
	    private String sentence;  //variables stored into each card

	    Card(String s){  //constructor
	        sentence = s;
	    }

	    //getters and setters
	    String getSentence() {
	        return sentence;
	    }
	}
	
	public void printDeck(GameCommands deck)
	{
		if(Game.matchCommand(deck, GameCommands.DECK_SCENARIO))
		{
			printDeck(scenarios);
		}
		else if(Game.matchCommand(deck, GameCommands.DECK_ANSWERS))
		{
			printDeck(answers);
		}
	}
	
	public void printDeck(ArrayList<Card> deck)
	{
		if(deck != null)
		{
		    for (int i = 0; i < deck.size(); i++) {
		    	System.out.println(deck.get(i).getSentence());
		    }
		}
		else
			System.out.println("Loading ...");
	}
	
	private ArrayList<Card> initDeck(String filePath)
	{
		ArrayList<Card> deck = new ArrayList<Card>();
		ClassLoader classLoader = getClass().getClassLoader();
		
		try {
			File file = new File(classLoader.getResource(filePath).getFile());
            BufferedReader reader = new BufferedReader(new FileReader(file)); //reads answer cards from file
            String line = reader.readLine();
            
            while (line != null) {
            	
            	deck.add(new Card(line));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		Collections.shuffle(deck); //randomize order
		
		return deck;
	}
	
	public static GameCommands stringToCommand(String command) {

	    for (GameCommands c : GameCommands.values()) {
	        if (c.name().equals(command)) {
	            return c;
	        }
	    }

	    return GameCommands.MISC_MESSAGE; //cant find command? fallback to misc
	}
	
	public static boolean matchCommand(String input, GameCommands command)
	{
		return input.startsWith(command.toString());
	}
	
	public static boolean matchCommand(GameCommands command, GameCommands commandTwo)
	{
		return command.toString().startsWith(commandTwo.toString());
	}
	
	static void print(Object obj)
	{
		System.out.println(obj);
	}

}
