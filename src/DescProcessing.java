import java.util.ArrayList;

/**
 * Структура хода
 * @author ataryq
 *
 */
class Move {
	Move(int _x, int _y, char _type){x = _x; y = _y; type = _type;}
	int x, y;
	char type;
}

/**
 * Логика работы с доской: проверка ходов на корректность, 
 * добаление, 
 * удаление окруженных камней 
 * @author ataryq
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
	
	/**
	 * Коструктор
	 * @param size_desc  разер доски
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
	 * Вернет тип
	 * @param type true - black
	 * @return
	 */
	protected char GetTypeByBool(boolean type) {
		if(type) return BLACK;
		else return WHITE;
	}
	
	protected char GetAnotherType(char type) {
		if(type == WHITE) return BLACK;
		else if(type == BLACK) return WHITE;
		else return EMPTY;
	}
	
	/**
	 * Сохранить состояние доски в буфер
	 */
	public void SaveState() {
		for(int i = 0; i < size; i++) {
			for(int k = 0; k < size; k++) {
				save_state[i][k] = desc[i][k];
			}
		}
	}
	
	/**
	 * Восстановить состояние доски из буфера
	 */
	public void LoadState() {
		for(int i = 0; i < size; i++) {
			for(int k = 0; k < size; k++) {
				desc[i][k] = save_state[i][k];
			}
		}
	}
	
	/**
	 * Проверка хода на корректность: на то что позиция не повторяет предыдущую позицию,
	 * что ход возможен
	 * @param X  поз. по х
	 * @param Y поз. по у
	 * @param type цвет камня
	 * @return true - ход одобрен, false - иначе
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
	 * Рекурсивная проверка, если позиция в которую хотят поставить камень окружена, запретить
	 * @param X поз. х
	 * @param Y поз. у
	 * @param type цвет камня
	 * @return true - ход одобрен, false - иначе
	 */
	protected boolean CheckSuicideMove(int X, int Y, char type) {
		if(RecProcSurrStone(X, Y, type)) return true;
		return false;
	}
	
	/**
	 * Сохранить состояние доски в буфер последнего хода
	 * @param type цвет камня
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
	
	public void PutStone(int x, int y, char type) {
		desc[x][y] = type;
	}
	
	/**
	 * удалит окруженные камни противника
	 * @param type цвет камня
	 * @return список удаленных камней
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
	
	// function find killed groups
	// ret false if group must delete
	/**
	 * Рекрсивный алгоритм поиска мертвых групп
	 * @param x поз. х
	 * @param y поз. у
	 * @param type цвет камня
	 * @return true - группа включающая камень в позиции х, у жива, false - иначе
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
	 * Для тестирования
	 * Тестовая функция для проверки работы алгоритма поиска мертвых групп
	 */
	public void Test() {		
		desc[2][3] = BLACK;
		desc[2][4] = BLACK;
		desc[2][5] = BLACK;
		
		desc[3][2] = BLACK;
		desc[3][6] = BLACK;
		desc[4][3] = BLACK;
		
		desc[4][4] = BLACK;
		desc[4][6] = BLACK;
		desc[5][5] = BLACK;
		
		desc[3][3] = WHITE;
		desc[3][4] = WHITE;
		desc[3][5] = WHITE;
		desc[4][5] = WHITE;
		ArrayList<Move> dm = ProcSurroundStone(BLACK);
		
		for(int i = 0; i < dm.size(); i++) {
			ServerProcessing.Log("type: " + dm.get(i).type + "\n");
		}
		
		PrintDesc();                                                  
		
		dm = ProcSurroundStone(WHITE);
		
		for(int i = 0; i < dm.size(); i++) {
			System.out.println("type: " + dm.get(i).type + "\n");
		}
		
		PrintDesc();
	}
	
	/**
	 * Для тестирования
	 * Вывод доски в консоль разработчика
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
