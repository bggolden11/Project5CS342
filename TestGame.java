package projectFive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import projectFive.Game.GameCommands;

class TestGame {

	@Test
	void testRockCommand() {
		assertEquals("Game Command: Rock not present", Game.GameCommands.PLAY_ROCK.toString(), "PLAY_ROCK");
		
	}
	
	@Test
	void testSpockCommand() {
		assertEquals("Game Command: Spock not present", Game.GameCommands.PLAY_SPOCK.toString(), "PLAY_SPOCK");
		
	}
	
	@Test
	void testScissors() {
		assertEquals("Game Command: Scissors not present", Game.GameCommands.PLAY_SCISSORS.toString(), "PLAY_SCISSORS");
		
	}
	
	@Test
	void testConvertCommand() {
		assertTrue("Game Command: Rock conversion invalid", Game.stringToCommand("PLAY_ROCK").equals(GameCommands.PLAY_ROCK));
		
	}
	
	@Test
	void testConvertCommand2() {
		assertTrue("Game Command: Lizard conversion invalid", Game.stringToCommand("PLAY_LIZARD").equals(GameCommands.PLAY_LIZARD));
		
	}
	
	@Test
	void testConvertCommand3() {
		assertTrue("Game Command: Spock conversion invalid", Game.stringToCommand("PLAY_SPOCK").equals(GameCommands.PLAY_SPOCK));
		
	}
}
