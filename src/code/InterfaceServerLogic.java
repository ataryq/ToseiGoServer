package code;
import java.net.Socket;

/**
 * ��������� ������ �������
 * @author ataryq
 *
 */
public interface InterfaceServerLogic {
	/**
	 * �������� ����� �����������
	 * @param new_user
	 */
	public void AddNewConnection(Socket new_user);
	/**
	 * ���������� ����� ������
	 * @param _base_server
	 */
	public void SetBaseServer(InterfaceServerBase _base_server);
	/**
	 * ������ �������
	 */
	public void Start();
	/**
	 * ������� �����������
	 * @param login �����
	 * @return true - ������������ ������� ����������, false - �����
	 */
	public boolean RegistredNewUser(String login);
	/**
	 * �������� ������ �������� ���������� ��������� find_str, ����� ������ ����������� �������
	 * @param _client ���������� ������
	 * @param find_str ������� ���������
	 * @return ������ ������� ������������, ����������� � ����
	 */
	public String[] GetClients(InterfaceClient _client, String find_str);
	/**
	 * ����� ������������ �� ������
	 * @param login �����
	 * @return ������ ������������
	 */
	public InterfaceClient FindPlayerByLogin(String login);
	/**
	 * �������� �������
	 * @param cl
	 */
	public void DeleteClient(InterfaceClient cl);
	/**
	 * �������� ����������� ���� ������� �� �������
	 * @return ����� �������
	 */
	public int GetNumPlayers();
}
