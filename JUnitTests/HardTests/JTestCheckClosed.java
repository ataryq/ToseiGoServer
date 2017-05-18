package HardTests;

import static org.junit.Assert.*;

import org.junit.Test;

import code.DescProcessing;

public class JTestCheckClosed {

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

		Integer counter = new Integer(0);
		boolean ret = descProcess.CheckClosedGroup(5, 5, counter);
		if(!ret) {
			fail("Invalid check to closed group");
		}

		
	}

}
