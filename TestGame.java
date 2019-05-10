package projectFive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import projectFive.Game.GameCommands;

class TestGame {
	
	@Test
	void testConvertCommand() {
		assertTrue("Game Command: Misc message invalid", Game.stringToCommand("MISC_MESSAGE").equals(GameCommands.MISC_MESSAGE));
		
	}

}
