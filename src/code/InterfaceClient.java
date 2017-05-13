package code;
import java.util.ArrayList;

/**
 * �������� ������ �������
 * @author ataryq
 *
 */
public interface InterfaceClient{
	public final int AUTHORIZATE = 0;
	public final int IN_MENU = 1;
	public final int PRECESSING_GAME = 2;
	public final int END_GAME = 3;
	
	/**
	 * @return true - ������������ �����������, false - �����
	 */
	public boolean IsRegistred();
	public String GetLogin();

	/**
	 * ���������� ��������� �������
	 * @param _state ����� ���������
	 */
	public void SetState(int _state);
	/**
	 * �������� ������ �� ���� ������������
	 * @param _game
	 */
	public void SetGame(ProcessGame _game);
	/**
	 * �������� ������ �� ����
	 * @return ������ �� ����
	 */
	public ProcessGame GetGame();
	/**
	 * ����� �� ����������� ������
	 */
	public void RefusedGame();
	/**
	 * ��������� ��� ����������
	 */
	public void SendMove(int X, int Y);
	/**
	 * ���������, ��� ��� ����������
	 */
	public void SendRefusedMove();
	/**
	 * �������� ����
	 */
	public void SendPass();
	/**
	 * ��������� ����� � ������������ ����
	 * @param accept true - ��� �������, false - �����
	 */
	public void SendAnswerMove(boolean accept);
	/**
	 * �������� ����������
	 * ������ �� ������������, ��� ������� ���
	 */
	public boolean CheckConnection();
	/**
	 * ���������� � ��������(�������� ����������, �������� ��������)
	 */
	public void PrepareDelete();
	/**
	 * ������� ������� ��������� �������
	 * @return ��������� �������
	 */
	public int GetState();
	/**
	 * ���������� ����������
	 */
	public void Unconnected();
	/**
	 * ������� ������� � ������ �� �����
	 * @param moves
	 */
	public void SendDiffMove(ArrayList<DescProcessing.Move> moves);
	/**
	 * ������� ����
	 * @param point ���� ������
	 * @param opp_point ���� ���������
	 */
	public void SendPoint(int point, int opp_point);
	/**
	 * �������� � ���������� ����
	 */
	public void SendEndGame();
	/**
	 * ��������� �� ������������
	 * @return true - ���� ���������, false - �����
	 */
	public boolean IsInvited();
	/**
	 * ���������� ��������� �� ������������
	 * @param _invited true - ���� ���������, false - �����
	 */
	public void SetInvited(boolean _invited);
	/**
	 * �������� �����������
	 */
	public void CancelInvite();
	
	/**
	 * ���������� ������������
	 * @param name_inviter ����� ������������
	 */
	public void Invite(String name_inviter);
	/**
	 * ������ ����
	 * @param color ���� ������ ������
	 * @param num_players ����������� �������
	 */
	public void StartGame(boolean color, int num_players);
}
