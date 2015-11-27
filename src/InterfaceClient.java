import java.util.ArrayList;


public interface InterfaceClient{
	public final int AUTHORIZATE = 0;
	public final int IN_MENU = 1;
	public final int PRECESSING_GAME = 2;
	public final int END_GAME = 3;
	
	public boolean IsRegistred();
	public String GetLogin();
	public boolean SendInvite(String login);
	public void SetState(int _state);
	public void SetGame(ProcessGame _game);
	public ProcessGame GetGame();
	public void RefusedGame();
	public void SendMove(int X, int Y);
	public void SendRefusedMove();
	public void SendPass();
	public void SendAnswerMove(boolean accept);
	public boolean CheckConnection();
	public void PrepareDelete();
	public int GetState();
	public void Unconnected();
	public void SendDiffMove(ArrayList<Move> moves);
	public void SendPoint(int point, int opp_point);
	public void SendEndGame();
	public boolean IsInvited();
	public void SetInvited(boolean _invited);
	public void CancelInvite();
	public void Invite(String name_inviter);
	public void StartGame(boolean color, int num_players);
}
