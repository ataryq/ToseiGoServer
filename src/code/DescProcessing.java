package code;
import java.util.ArrayList;

/**
 * Working with playing desc
 * There is have algorithm for working with desc
 * @author ataryq
 *
 */
public class DescProcessing {
	char [][] desc;
	int size;
	// desc two moves old
	char [][] last_desc_black;
	char [][] last_desc_white;
	char [][] save_state;
	public ArrayList<Move> diff;
	private boolean simulate = false;
	
	protected boolean [][] rec_desc;
	protected ArrayList<Move> proc_stone;
	
	public static final char BLACK = '0';
	public static final char WHITE = '1';
	public static final char EMPTY = '2';
	public static final char NOT_FILL = '3';
	
	/**
	 * Stcuture of move.
	 *
	 */
	public class Move {
		Move(int _x, int _y, char _type){x = _x; y = _y; type = _type;}
		Move(){type = NOT_FILL;}
		public int x, y;
		public char type;
	}
	
	
	/**
	 * Constructor
	 * @param size_desc - size of desc
	 */
	public DescProcessing(int size_desc) {
		size = size_desc;
		desc = new char[size][size];
		rec_desc = new boolean [size][size];
		last_desc_black = new char [size][size];
		last_desc_white = new char [size][size];
		save_state = new char [size][size];
		proc_stone = new ArrayList<Move>();
		diff = new ArrayList<Move>();
		
		for(int i = 0; i < size; i++) {
			for(int k = 0; k < size; k++) {
				desc[i][k] = EMPTY;
				rec_desc[i][k] = false;
				last_desc_black[i][k] = BLACK;
				last_desc_white[i][k] = BLACK;
			}
		}	
	}
	
	/**
	 * Return type
	 * @param type - type of player
	 * @return color of player
	 */
	protected char GetTypeByBool(boolean type) {
		if(type) return BLACK;
		else return WHITE;
	}
	
	/**
	 * Get another player.
	 * @param type black or white player
	 * @return another player
	 */
	protected char GetAnotherType(char type) {
		if(type == WHITE) return BLACK;
		else if(type == BLACK) return WHITE;
		else return EMPTY;
	}
	
	/**
	 * Save state desc to buffer.
	 */
	public void SaveState() {
		for(int i = 0; i < size; i++) {
			for(int k = 0; k < size; k++) {
				save_state[i][k] = desc[i][k];
			}
		}
	}

	/**
	 * Load state desc from buffer.
	 */
	public void LoadState() {
		for(int i = 0; i < size; i++) {
			for(int k = 0; k < size; k++) {
				desc[i][k] = save_state[i][k];
			}
		}
	}
	
	/**
	 * Check move to correct.
	 * @param X
	 * @param Y
	 * @param type - type stone
	 * @return true - accept move, false - else
	 */
	public boolean CheckMove(int X, int Y, char type) {
		boolean ret = false;
		
		if(desc[X][Y] != EMPTY) return false;
		
		ServerProcessing.Log("check move start t: " + type + "\n");
		
		simulate = true;
		SaveState();
		
		this.PutStone(X, Y, type);
		this.ProcSurroundStone(GetAnotherType(type));
				
		if(type == BLACK) {
			for(int i = 0; i < size; i++) {
				for(int k = 0; k < size; k++) {
					if(last_desc_black[i][k] != desc[i][k] ) ret = true;
				}
			}
		} else {
			for(int i = 0; i < size; i++) {
				for(int k = 0; k < size; k++) {
					if(last_desc_white[i][k] != desc[i][k] ) ret = true;
				}
			}
		}
		
		ret &= CheckSuicideMove(X, Y, type);
		LoadState();
		simulate = false;
		
		System.out.println("check move end ret: " + ret);
		
		return ret;
	}
	
	/**
	 * Check move to suicide.
	 * @param X
	 * @param Y
	 * @param type - type stone 
	 * @return true - move is suicide, false - else
	 */
	public boolean CheckSuicideMove(int X, int Y, char type) {
		if(RecProcSurrStone(X, Y, type)) return true;
		return false;
	}
	
	/**
	 * Save desc state to buffer of last move.
	 * @param type - type of stone
	 */
	protected void SaveStateDesc(char type) {
		if(type == BLACK) 
		{
			ServerProcessing.Log("save black \n");
			for(int i = 0; i < size; i++) {
				for(int k = 0; k < size; k++) {
					last_desc_black[i][k] = desc[i][k];
				}
			}
		} else 
		{
			System.out.println("save white");
			for(int i = 0; i < size; i++) {
				for(int k = 0; k < size; k++) {
					last_desc_white[i][k] = desc[i][k];
				}
			}
		}
	}
	
	/**
	 * Put stone into desc.
	 * @param x 
	 * @param y
	 * @param type - type of stone
	 */
	public void PutStone(int x, int y, char type) {
		desc[x][y] = type;
	}
	
	/**
	 * 
	 * @param type of stone
	 * @return list of deleted stones
	 */
	public ArrayList<Move> ProcSurroundStone(char type) {
		if(!simulate) SaveStateDesc(GetAnotherType(type));
		
		for(int i = 0; i < size; i++) {
			for(int k = 0; k < size; k++) {
				rec_desc[i][k] = false;
			}
		}
		
		proc_stone.clear();
		for(int i = 0; i < size; i++) {
			for(int k = 0; k < size; k++) {
				if(!rec_desc[i][k]) 
				{
					if(!RecProcSurrStone(i, k, type) ) {
						for(int m = 0; m < proc_stone.size(); m++) {
							Move mv = proc_stone.get(m);
							desc[mv.x][mv.y] = EMPTY;
							
							mv.type = EMPTY;
							if(!simulate) diff.add(new Move(mv.x, mv.y, mv.type));
						}
					}
				}
				
				proc_stone.clear();
			}
		}
		
		//PrintDesc();
		
		return diff;
	}
	
	/**
	 * function find killed groups
	 * @param x - coordinate of start move
	 * @param y - coordinate of start move
	 * @param type - type of move
	 * @return true - group of stone is alive, false - else
	 */
	protected boolean RecProcSurrStone(int x, int y, char type) {
		if( !(x >= 0 && x < this.size &&
				y >= 0 && y < size) ) return false;
		if(desc[x][y] == EMPTY) return true;
		if(rec_desc[x][y]) return false;
		if(desc[x][y] == type) {
			rec_desc[x][y] = true;
			proc_stone.add(new Move(x, y, type));
			
			boolean ret = false;
			
			if(x >= 0) 		ret |= RecProcSurrStone(x - 1, y, type);
			if(x < size) 	ret |= RecProcSurrStone(x + 1, y, type);
			if(y >= 0) 		ret |= RecProcSurrStone(x, y - 1, type);
			if(y < size) 	ret |= RecProcSurrStone(x, y + 1, type);
			return ret;
		}
		
		return false;
	}
			
	/**
	 * 
	 * @param x_start x coordinate of start move
	 * @param y_start y coordinate of start move
	 * @param counter size group
	 * @return true - closed group, false - else
	 */
	protected boolean CheckClosedGroup(int x_start, int y_start, Integer counter) {
		RecCheckClosedGroup(new Move(x_start, y_start, 
				desc[x_start][y_start]), new Move(), counter);
		if(counter < (size * size / 2) ) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param new_m	new move
	 * @param neighbors array with neighbors
	 * @return count of neighbors
	 */
	protected int FindCountNeighbor(Move new_m, ArrayList<Move> neighbors) {
		int ret = 0;
		if(new_m.x + 1 < size) {
			if(desc[new_m.x + 1][new_m.y] == new_m.type) {
				ret += 1;
				Move new_neighbor = new Move(new_m.x + 1, new_m.y, new_m.type);
				neighbors.add(new_neighbor);
			}
		}
		
		if(new_m.x > 0) {
			if(desc[new_m.x - 1][new_m.y] == new_m.type) {
				ret += 1;
				Move new_neighbor = new Move(new_m.x + 1, new_m.y, new_m.type);
				neighbors.add(new_neighbor);
			}
		}
		
		if(new_m.y + 1 < size) {
			if(desc[new_m.x][new_m.y + 1] == new_m.type) {
				ret += 1;
				Move new_neighbor = new Move(new_m.x + 1, new_m.y, new_m.type);
				neighbors.add(new_neighbor);
			}
		}
		
		if(new_m.y > 0) {
			if(desc[new_m.x][new_m.y - 1] == new_m.type) {
				ret += 1;
				Move new_neighbor = new Move(new_m.x + 1, new_m.y, new_m.type);
				neighbors.add(new_neighbor);
			}
		}
		
		return ret;
	}
	
	/**
	 * Recursive part of algorithm finding closed group
	 * @param new_m new move
	 * @param old_m old move
	 * @param counter count of moves
	 */
	protected void RecCheckClosedGroup(Move new_m, Move old_m, int counter) {
		ArrayList<Move> neighbors = new ArrayList<Move>();
		counter++;
		if( FindCountNeighbor(new_m, neighbors) == 0 ) {
			return;
		}
		if( FindCountNeighbor(new_m, neighbors) == 1 ) {
			if(old_m.type != NOT_FILL) {
				return;
			}
		}
		for(int i = 0; i < neighbors.size(); i++) {
			RecCheckClosedGroup(neighbors.get(i), new_m, counter);
		}
	}

	/**
	 * Check group for deathless
	 * @param x_start x coordinate of start move
	 * @param y_start y coordinate of start move
	 * @return true - deathless, false - not deathless
	 */
	public boolean CheckDeathlessGroup(int x_start, int y_start) {
		Integer counter = new Integer(0);
		boolean closed = CheckClosedGroup(x_start, y_start, counter);
		if(closed) {
			int countFreeSpace = CalcFreeSapceInClosedGroupe(x_start, y_start);
			if(countFreeSpace == counter) {
				return false;
			}
			else {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param x_start x coordinate of start move
	 * @param y_start y coordinate of start move
	 * @return number of free space inside of closed group
	 */
	protected int CalcFreeSapceInClosedGroupe(int x_start, int y_start) {
		ArrayList<Move> freeSpace = new ArrayList<Move>();
		Move m = new Move(x_start, y_start, desc[x_start][y_start]);
		return FindCountFreeSpace(m, freeSpace);
	}
	
	/**
	 * 
	 * @param new_m move checking
	 * @param freeSpace array with neighbors
	 * @return count of neighbors
	 */
	protected int FindCountFreeSpace(Move new_m, ArrayList<Move> freeSpace) {
		int ret = 0;
		if(new_m.x + 1 < size) {
			if(desc[new_m.x + 1][new_m.y] != new_m.type) {
				ret += 1;
				Move new_neighbor = new Move(new_m.x + 1, new_m.y, 
						desc[new_m.x + 1][new_m.y]);
				freeSpace.add(new_neighbor);
			}
		}
		
		if(new_m.x > 0) {
			if(desc[new_m.x - 1][new_m.y] != new_m.type) {
				ret += 1;
				Move new_neighbor = new Move(new_m.x + 1, new_m.y, 
						desc[new_m.x - 1][new_m.y]);
				freeSpace.add(new_neighbor);
			}
		}
		
		if(new_m.y + 1 < size) {
			if(desc[new_m.x][new_m.y + 1] != new_m.type) {
				ret += 1;
				Move new_neighbor = new Move(new_m.x + 1, new_m.y, 
						desc[new_m.x][new_m.y + 1]);
				freeSpace.add(new_neighbor);
			}
		}
		
		if(new_m.y > 0) {
			if(desc[new_m.x][new_m.y - 1] != new_m.type) {
				ret += 1;
				Move new_neighbor = new Move(new_m.x + 1, new_m.y, 
						desc[new_m.x][new_m.y - 1]);
				freeSpace.add(new_neighbor);
			}
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @param x coordinate of start move
	 * @param y coordinate of start move
	 * @return
	 */
	public char GetTypeStone(int x, int y) { 
		return desc[x][y];
	}
	
	/**
	 * for testing
	 * out to consol
	 */
	public void PrintDesc() {
		System.out.print("\n______________________________________\n");
		for(int i = 0; i < size; i++) {
			for(int k = 0; k < size; k++) {
				System.out.print(desc[i][k] + "|"); 
			}
			System.out.print("\n");
		}
		System.out.print("______________________________________\n");

	}
	
}