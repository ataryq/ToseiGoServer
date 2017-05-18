package HardTests;

import static org.junit.Assert.*;

import org.junit.Test;

import code.DescProcessing;

/**
 * Test group must be killed
 */

public class JTestDeleteSurroundStone {

	@Test
	public void test() {
		final int size = 19;
		DescProcessing descProcess = new DescProcessing(size);
		descProcess.PutStone(2, 3, DescProcessing.BLACK);
		descProcess.PutStone(2, 4, DescProcessing.BLACK);
		descProcess.PutStone(2, 5, DescProcessing.BLACK);

		descProcess.PutStone(3, 2, DescProcessing.BLACK);
		descProcess.PutStone(3, 6, DescProcessing.BLACK);
		descProcess.PutStone(4, 3, DescProcessing.BLACK);

		descProcess.PutStone(4, 4, DescProcessing.BLACK);
		descProcess.PutStone(4, 6, DescProcessing.BLACK);
		descProcess.PutStone(5, 5, DescProcessing.BLACK);

		descProcess.PutStone(3, 3, DescProcessing.WHITE);
		descProcess.PutStone(3, 4, DescProcessing.WHITE);
		descProcess.PutStone(3, 5, DescProcessing.WHITE);
		descProcess.PutStone(4, 5, DescProcessing.WHITE);

		descProcess.PrintDesc();

		descProcess.ProcSurroundStone(DescProcessing.WHITE);

		descProcess.PrintDesc();
		
		if(descProcess.GetTypeStone(3, 3) != DescProcessing.EMPTY ||
				descProcess.GetTypeStone(3, 4) != DescProcessing.EMPTY ||
				descProcess.GetTypeStone(3, 5) != DescProcessing.EMPTY ||
				descProcess.GetTypeStone(4, 5) != DescProcessing.EMPTY) {
			fail("Invalid processing surround");
		}

		
	}

}
