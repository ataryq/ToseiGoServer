package AutomaticTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import code.DescProcessing;
import code.DescProcessing.ExeptionOutOfDescSize;
import code.DescProcessing.Move;

public class JTestAutoDeleteSurroundStone {
	public final int size = GenerateField.SIZE;
	private final int NUM_AUTOTESTS = 10;
	private int length;
	private int x_start;
	private int y_start;
	private DescProcessing descProcess;
	ArrayList<Move> groupInside;
	
	@Test
	public void test() {
		for(int i = 0; i < NUM_AUTOTESTS; i++) {
			ProcessTest();
		}
	}
	
	void ProcessTest() {
		
		for(int i = 0; i < NUM_AUTOTESTS; i++) {
			descProcess = new DescProcessing(size);
			length =  (int) (Math.random() * 8) + 2;
			x_start = (int) (Math.random() * 12);
			y_start = (int) (Math.random() * 12);
			
			PutOuterContour();
	
			FillInsideMoves();
			
			descProcess.PrintDesc();
			descProcess.ProcSurroundStone(DescProcessing.WHITE);
			descProcess.PrintDesc(); 
			
			CheckResult();
		}
	}
	
	private void CheckResult() {
		for(int i = 0; i < groupInside.size(); i++) {
			Move mv = groupInside.get(i);
			if(descProcess.GetTypeStone(mv.x, mv.y) != DescProcessing.EMPTY) {
				fail("Autotest delete surround stone is invalid");
				System.out.print("Fail!");
			}
		}
	}
	
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
				try {
					descProcess.PutStone(mv);
					groupInside.add(mv);
				} catch (ExeptionOutOfDescSize e) {}
			}
		}
	}

}
