package code;
import java.util.ArrayList;

/**
 * ���������� ������ �������, ���, ��������
 * @author ataryq
 *
 */
public class Controller {
	//������
	InterfaceServerLogic serv;
	//������ ���
	ArrayList<ProcessGame> games;
	
	public Controller() {
		games = new ArrayList<ProcessGame>();
	}
	
	/**
	 * ��������� ������ �������� ��� ����������� ������, ���������� ��������� ������
	 * @param _client ������ ������� ����������� ������ �������
	 * @param find_str ������ ������ 
	 * @return ������ ��������
	 */
	public String[] GetClients(InterfaceClient _client, String find_str) {
		return serv.GetClients(_client, find_str);
	}
	
	/**
	 * �������������
	 * @param _serv ������
	 */
	public void Init(InterfaceServerLogic _serv) {
		serv = _serv;
	}
	
	/**
	 * ������� �����������
	 * @param login �����
	 * @param pl ������ �����
	 * @return true - ���� ����������� ������ �������, ����� false
	 */
	public boolean RegistredNewUser(String login, InterfaceClient pl) {
		if( serv.RegistredNewUser(login) ) {
			ServerProcessing.Log("go to state IN_MENU \n");
			pl.SetState(InterfaceClient.IN_MENU);
			
			return true;
		} else return false;
	}
	
	/**
	 * ����������� ������
	 * @param host ������ �����
	 * @param login ����� ������������� ������������
	 */
	public void InvitePlayer(InterfaceClient host, String login) {
		InterfaceClient asked = serv.FindPlayerByLogin(login);
		if(asked == null || 
				asked.IsInvited() || 
				asked.GetState() != InterfaceClient.IN_MENU) {
			host.RefusedGame();
			return;
		}
		host.SetInvited(true);
		asked.Invite(host.GetLogin());
		ProcessGame new_game = new ProcessGame(host, asked, this);
		games.add(new_game);
		host.SetGame(new_game);
		asked.SetGame(new_game);
	}
	
	/**
	 * �������� ����������� �������������
	 * @return ����������� �������������� �������������
	 */
	public int GetNumPlayers() {
		return serv.GetNumPlayers();
	}
	
	/**
	 * �������� ������������
	 * @param cl ������ ������������
	 */
	public void Unconnect(InterfaceClient cl) {
		serv.DeleteClient(cl);
		
	}
	
	/**
	 * ��������� ������
	 * @param _game ������ ������
	 */
	public void EndGame(ProcessGame _game) {
		_game.EndGame();
		DeleteGame(_game);
	}
	
	/**
	 * ������� ������
	 * @param _game ������ ������
	 */
	public void DeleteGame(ProcessGame _game) {
		ProcessGame cur_game;
		for(int i = 0; i < games.size(); i++) {
			cur_game = games.get(i);
			if(cur_game == _game) {
				cur_game.PrepareDelete();
				games.remove(i);
			}
		}
		ServerProcessing.Log("num games: " + games.size() + "\n");
	}
	
	/**
	 * �������� ������ �� �����������
	 * @param cl ������������ ������������
	 */
	public void CancelRequestInvite(InterfaceClient cl) {
		cl.GetGame().CancelInvite();
		DeleteGame(cl.GetGame());
	}
	
	/**
	 * ����� �� ����������� ����
	 * @param responce true - �������� ����������, false - �����
	 * @param cl ������������ ������������
	 */
	public void ResponceInvite(boolean responce, InterfaceClient cl) {
		if(responce) {
			cl.GetGame().StartGame();
		} else {
			cl.SetInvited(false);
			InterfaceClient cl_an = cl.GetGame().GetAnother(cl);
			cl_an.RefusedGame();
			DeleteGame(cl.GetGame());
		}
	}
}
