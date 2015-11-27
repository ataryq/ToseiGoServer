

public class ProcessGame {
	public static final boolean BLACK = true;
	public static final boolean WHITE = false;
	
	protected InterfaceClient black; 
	protected InterfaceClient white;
	protected boolean is_started_game = false;
	protected boolean was_pass = false;
	protected Controller control; 
	protected boolean queue_move = true;
	protected DescProcessing desc_proc;
	protected int size_desc;
	protected int white_point = 0;
	protected int black_point = 0;
	
	public ProcessGame(InterfaceClient _host, 
			InterfaceClient _pl2,
			Controller _control) {
		black = _host;
		white = _pl2;
		control = _control;
		size_desc = 19;
		desc_proc = new DescProcessing(size_desc);
	}
		
	public InterfaceClient GetHost() {
		return black;
	}
	
	protected boolean CheckMove(int X, int Y, boolean type) {
		if(X < size_desc && Y >= 0 && Y < size_desc && Y >= 0) {
			if(type == BLACK) 
				return desc_proc.CheckMove(X, Y, DescProcessing.BLACK);
			else 
				return desc_proc.CheckMove(X, Y, DescProcessing.WHITE);
		}
		else return false;
	}
	
	public void Move(int X, int Y, InterfaceClient player) {
		try {
			boolean type;
			if(player == black) type = BLACK;
			else type = WHITE;

			ServerProcessing.Log("proc game Move \n");
			if((player == black && queue_move == BLACK) ||
					(player == white && queue_move == WHITE) )
			{
					
				if(CheckMove(X, Y, type)) 
				{
					char color, other_color;
					if(player == white) {
						color = DescProcessing.WHITE;
						other_color = DescProcessing.BLACK;
					}
					else {
						color = DescProcessing.BLACK;
						other_color = DescProcessing.WHITE;
					}
					
					desc_proc.PutStone(X, Y, color);
					desc_proc.ProcSurroundStone(other_color);
					
					if(desc_proc.diff.size() > 0) {
						ServerProcessing.Log("send points");
						if(player == white) white_point += desc_proc.diff.size();
						else black_point += desc_proc.diff.size();
						white.SendPoint(black_point, white_point);
						black.SendPoint(black_point, white_point);
					}
					
					
					GetAnother(player).SendMove(X, Y);
					player.SendAnswerMove(true);
					
					for(int i = 0; i < desc_proc.diff.size(); i++) {
						ServerProcessing.Log("type diff " + desc_proc.diff.get(i).type + "\n");
					}
					
					if(desc_proc.diff.size() > 0) {
						player.SendDiffMove( desc_proc.diff );
						GetAnother(player).SendDiffMove( desc_proc.diff );
						desc_proc.diff.clear();
					}
					
					was_pass = false;
					queue_move = !queue_move;
				} else {
					player.SendAnswerMove(false);
				}
	
			} else {
				ServerProcessing.Log("not in queue \n");
				player.SendAnswerMove(false);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	public void Pass(InterfaceClient player) {
		ServerProcessing.Log("try pass \n");
		if((player == black && queue_move == BLACK) ||
				(player == white && queue_move == WHITE) ) 
		{
			ServerProcessing.Log("pass \n");
			GetAnother(player).SendPass();
			queue_move = !queue_move;
			if(was_pass) {
				EndGame();
			}
			was_pass = true;
		} else {
			ServerProcessing.Log("not in queue \n");
		}
	}
	
	public void EndGame() {
		ServerProcessing.Log("end game \n");
		if(this.is_started_game) {
			black.SendEndGame();
			white.SendEndGame();
		} else {
			black.CancelInvite();
			white.CancelInvite();
		}
		black.SetState(InterfaceClient.IN_MENU);
		white.SetState(InterfaceClient.IN_MENU);
		control.DeleteGame(this);
	}
	
	public void StartGame() {
		is_started_game = true;
		black.StartGame(BLACK, control.GetNumPlayers());
		white.StartGame(WHITE, control.GetNumPlayers());
		black.SetState(Client.PRECESSING_GAME);
		white.SetState(Client.PRECESSING_GAME);
	}
	
	public boolean GetStartedGame() {
		return this.is_started_game;
	}
	
	public InterfaceClient GetAnother(InterfaceClient cl) {
		if(cl == black) return white;
		else if(cl == white) return black;
		else {
			ServerProcessing.Log("error! player not recognized \n");
			return null;
		}
	}
	
	public void CancelInvite() {
		black.CancelInvite();
		white.CancelInvite();
	}
	
	public void PrepareDelete() {
		black.SetGame(null);
		white.SetGame(null);
	}
	
}
