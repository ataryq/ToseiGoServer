package IntegrationTests;

import static org.junit.Assert.*;

import org.junit.Test;

import code.DescProcessing;

public class JTestNotCheckMove {

	@Test
	public void test() {
		TestProgressGame game = new TestProgressGame();
				
		game.PutStone(2, 3, DescProcessing.BLACK);
		game.PutStone(2, 4, DescProcessing.BLACK);
		game.PutStone(2, 5, DescProcessing.BLACK);

		game.PutStone(3, 2, DescProcessing.BLACK);
		game.PutStone(3, 6, DescProcessing.BLACK);
		game.PutStone(4, 3, DescProcessing.BLACK);

		game.PutStone(4, 4, DescProcessing.BLACK);
		game.PutStone(4, 6, DescProcessing.BLACK);
		game.PutStone(5, 5, DescProcessing.BLACK);

		game.PutStone(3, 3, DescProcessing.WHITE);
		game.PutStone(3, 4, DescProcessing.WHITE);

		boolean res = game.TestCheckMove(4, 5, TestProgressGame.WHITE);
		
		if(!res) {
			fail("Test to check move is invalid");
		}
		
		
	}

}
