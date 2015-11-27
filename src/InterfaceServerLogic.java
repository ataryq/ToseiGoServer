import java.net.Socket;

public interface InterfaceServerLogic {
	public void AddNewConnection(Socket new_user);
	public void SetBaseServer(InterfaceServerBase _base_server);
	public void Start();
	public boolean RegistredNewUser(String login);
	public String[] GetClients(InterfaceClient _client, String find_str);
	public InterfaceClient FindPlayerByLogin(String login);
	public void DeleteClient(InterfaceClient cl);
	public int GetNumPlayers();
}
