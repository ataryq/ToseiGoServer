package AutomaticTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import code.DescProcessing;
import code.DescProcessing.ExeptionOutOfDescSize;
import code.DescProcessing.Move;

public class JUnitAutoCheckNotClosed {


	public final int size = GenerateField.SIZE;
	private final int NUM_AUTOTESTS = 10;
	private int length;
	private int x_start;
	private int y_start;
	private DescProcessing descProcess;
	ArrayList<Move> groupInside;

	private void PutOuterContour() {
		ArrayList<Move> group = GenerateField.GenerateNotClosedGroup(length, 
				x_start, y_start, DescProcessing.BLACK);
		for(int i = 0; i < group.size(); i++) {
			try {
				descProcess.PutStone(group.get(i));
			} catch (ExeptionOutOfDescSize e) {}
		}
	}
	
	@Test
	public void test() {
		for(int i = 0; i < NUM_AUTOTESTS; i++) {
			ProcessTest();
		}
	}
	
	void ProcessTest() {
		descProcess = new DescProcessing(size);
		length =  (int) (Math.random() * 4) + 3;
		x_start = (int) (Math.random() * 8) + 2;
		y_start = (int) (Math.random() * 8) + 2;
		
		PutOuterContour();
		
		descProcess.PrintDesc();
		descProcess.ProcSurroundStone(DescProcessing.WHITE);
		descProcess.PrintDesc(); 
		
		Integer counter = new Integer(0);
		boolean res = descProcess.CheckClosedGroup(x_start, y_start, counter);
		
		if(res) {
			fail("Testing to check not closed is invalid");
		}

	}

}
