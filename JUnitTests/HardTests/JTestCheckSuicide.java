package HardTests;

import static org.junit.Assert.*;

import org.junit.Test;

import code.DescProcessing;
import code.DescProcessing.ExeptionOutOfDescSize;

public class JTestCheckSuicide {
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
		
		boolean ret = false;
		try {
			ret = descProcess.CheckSuicideMove(4, 5, DescProcessing.WHITE);
		} catch (ExeptionOutOfDescSize e) {}
		
		if(ret) {
			descProcess.PrintDesc();
			fail("Test to check suicide moveing is invalid");
		}
	}

}
