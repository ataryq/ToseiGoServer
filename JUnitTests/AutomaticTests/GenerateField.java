package AutomaticTests;
import java.util.ArrayList;

import code.DescProcessing.Move;

public class GenerateField {
	public static final int SIZE = 19;
	
	public static ArrayList<Move> GenerateNotClosedGroup(int length, int x_start, int y_start, char type) {
		ArrayList<Move> group = new ArrayList<Move>();		
		for(int i = 0; i < 4; i++) {
			for(int k = 0; k < length; k++) {
				if(i == 0) group.add(new Move(x_start + k, y_start, type));
				if(i == 1) group.add(new Move(x_start, y_start + k, type));
			}
		}
		
		for(int i = 0; i < group.size(); i++) {
			if(group.get(i).x < 0 || 
					group.get(i).x >= SIZE ||
							group.get(i).y < 0 || 
							group.get(i).y >= SIZE) {
				group.remove(i);
				i--;
			}
		}

		return group;
	}
	
	public static ArrayList<Move> GenerateSquareGroup(int length, int x_start, int y_start, char type) {
		ArrayList<Move> squareGroup = new ArrayList<Move>();		
		
		for(int i = 0; i < 4; i++) {
			for(int k = 0; k < length; k++) {
				if(i == 0) squareGroup.add(new Move(x_start + k, y_start, type));
				if(i == 1) squareGroup.add(new Move(x_start, y_start + k, type));
				if(i == 2) squareGroup.add(new Move(x_start + k, y_start + length, type));
				if(i == 3) squareGroup.add(new Move(x_start + length, y_start + k, type));
			}
		}
		
		for(int i = 0; i < squareGroup.size(); i++) {
			if(squareGroup.get(i).x < 0 || 
					squareGroup.get(i).x >= SIZE ||
					squareGroup.get(i).y < 0 || 
					squareGroup.get(i).y >= SIZE) {
				squareGroup.remove(i);
				i--;
			}
		}

			
		return squareGroup;
	}
	
}
