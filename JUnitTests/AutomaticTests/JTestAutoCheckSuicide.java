package AutomaticTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import code.DescProcessing;
import code.DescProcessing.ExeptionOutOfDescSize;
import code.DescProcessing.Move;

public class JTestAutoCheckSuicide {
	
	public final int size = GenerateField.SIZE;
	private final int NUM_AUTOTESTS = 10;
	private int length;
	private int x_start;
	private int y_start;
	private DescProcessing descProcess;
	ArrayList<Move> groupInside;

	private void PutOuterContour() {
		ArrayList<Move> group = GenerateField.GenerateSquareGroup(length, 
				x_start, y_start, DescProcessing.BLACK);
		for(int i = 0; i < group.size(); i++) {
			try {
				descProcess.PutStone(group.get(i));
			} catch (ExeptionOutOfDescSize e) {}
		}
	}

	private void FillInsideMoves() {
		groupInside = new ArrayList<Move>();
		for(int i = 0; i < length - 1; i++) {
			for(int k = 0; k < length - 1; k++) {
				Move mv = new Move(x_start + 1 + i, y_start + 1 + k, DescProcessing.WHITE);
				if(i == length - 2 && k == length - 2) {
					continue;
				}
				try {
					descProcess.PutStone(mv);
					groupInside.add(mv);
				} catch (ExeptionOutOfDescSize e) {}
			}
		}
	}
	
	@Test
	public void test() {
		for(int i = 0; i < NUM_AUTOTESTS; i++) {
			ProcessTest();
		}
	}

	
	public void ProcessTest() {
		for(int i = 0; i < NUM_AUTOTESTS; i++) {
			descProcess = new DescProcessing(size);
			length =  (int) (Math.random() * 10);
			x_start = (int) (Math.random() * 19);
			y_start = (int) (Math.random() * 19);
	
			PutOuterContour();
			
			descProcess.PrintDesc();
			
			FillInsideMoves();
			for(int k = 0; k < groupInside.size(); k++) {
				System.out.print(groupInside.get(k).toString() + " ");
			}
			if(groupInside.isEmpty()) continue;
			descProcess.PrintDesc();
			System.out.print("check " + new Integer(x_start + length - 2).toString() + "," + new Integer(y_start + length - 2).toString());
			boolean res;
			try {
				res = descProcess.CheckSuicideMove(x_start + length - 2, y_start + length - 2, DescProcessing.WHITE);
			} catch (ExeptionOutOfDescSize e) {
				continue;
			}
			
			if(!res) {
				fail("Testing to check suicide moving is invalid");
			}
		}
	}

	

}
