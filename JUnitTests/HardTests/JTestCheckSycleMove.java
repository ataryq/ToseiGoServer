package HardTests;

import static org.junit.Assert.*;

import org.junit.Test;

import code.DescProcessing;

public class JTestCheckSycleMove {

	@Test
	public void test() {
		final int size = 19;
		DescProcessing descProcess = new DescProcessing(size);
		descProcess.PutStone(0, 0, DescProcessing.BLACK);
		descProcess.PutStone(1, 1, DescProcessing.BLACK);
		descProcess.PutStone(1, 0, DescProcessing.WHITE);
		descProcess.PutStone(2, 1, DescProcessing.WHITE);
		descProcess.PutStone(3, 0, DescProcessing.WHITE);
		descProcess.PrintDesc();
		
		
		descProcess.PutStone(2, 0, DescProcessing.BLACK);
		descProcess.ProcSurroundStone(DescProcessing.WHITE);
				
		if(!descProcess.CheckCycleMove(1, 0, DescProcessing.WHITE)) {
			fail("Check to sycle move is invalid");
		}
		
		descProcess.PutStone(1, 0, DescProcessing.WHITE);
		descProcess.ProcSurroundStone(DescProcessing.BLACK);
		
		if(descProcess.CheckCycleMove(2, 0, DescProcessing.BLACK)) {
			fail("Check to sycle move is invalid");
		}
		
		descProcess.PutStone(2, 0, DescProcessing.BLACK);
		descProcess.ProcSurroundStone(DescProcessing.WHITE);
		
	}

}
