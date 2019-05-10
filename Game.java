package projectFive;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Game {

	private static final String NEWLINE = "\n";
	private static final String DBLNEWLINE = "\n\n";
	
	private static final String GAME_SCENARIO_DICTIONARY = "src/serverScenarios.txt";
	private static final String GAME_ANSWERS_DICTIONARY = "src/serverAnswers.txt";
	
    private static ArrayList<Card> scenarios; //contains scenarios for cards against humanity
    private static ArrayList<Card> answers; //contains all the cards for deck
    
    public Game()
    {
    	initScenarios(GAME_SCENARIO_DICTIONARY);
    	initAnswers(GAME_ANSWERS_DICTIONARY);
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
	
	private void initScenarios(String filePath)
	{
		scenarios = new ArrayList<Card>();
		try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath)); //reads answer cards from file
            String line = reader.readLine();
            
            while (line != null) {
            	
            	scenarios.add(new Card(line));
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		Collections.shuffle(scenarios); //randomize order
	}
	
	private void initAnswers(String filePath)
	{
		answers = new ArrayList<Card>();
		try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath)); //reads answer cards from file
            String line = reader.readLine();
            
            while (line != null) {
            	
            	answers.add(new Card(line));
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		Collections.shuffle(answers); //randomize order
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
	
	static void print(Object obj)
	{
		System.out.println(obj);
	}

}
