package projectFive;

import java.io.*;

public class Game {

	private final String NEWLINE = "\n";
	private final String DBLNEWLINE = "\n\n";
	
	/*resource: https://www.baeldung.com/java-check-string-number*/
	public static boolean isInteger(String str) {
	    try {
	        int i = Integer.parseInt(str);
	    } catch (Exception e) {
	        return false;
	    }
	    
	    return true;
	}
	
	public enum GameCommands
	{
		CLIENT_WHOAMI,
		CLIENT_RESPONSE,
		CLIENT_LOBBY,
		
		PLAY_ROCK,
		PLAY_PAPER,
		PLAY_SCISSORS,
		PLAY_LIZARD,
		PLAY_SPOCK,
		
		MISC_MESSAGE,
	};
	
	public static GameCommands stringToCommand(String command) {

	    for (GameCommands c : GameCommands.values()) {
	        if (c.name().equals(command)) {
	            return c;
	        }
	    }

	    return GameCommands.MISC_MESSAGE;
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
