package code;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ����� ������, �������� �� ����������� ����� �����������
 * @author ataryq
 *
 */
public class SocketServer implements InterfaceServerBase, Runnable {
	
	ServerSocket serverSock;
	InterfaceServerLogic logicServer;
	final int port;
	
	public SocketServer(InterfaceServerLogic _logicServer) 
	{
		port = GameMain.port;

		try {
			serverSock = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logicServer = _logicServer;
		ServerProcessing.Log("create socket server \n");
	}
	
	/**
	 * ����� �������
	 */
	public void Start() 
	{
		ServerProcessing.Log("start socket server \n");
		Thread t = new Thread(this);		// �������� �������������� ������
		t.start();
	}
	
	/**
	 * ������������� ����������� � ������
	 */
	public void AcceptUsers() 
	{
		while(true) 
		{		// ������ ������������ �����
			Socket clientSocket = null;
			try {
				clientSocket = serverSock.accept();
				ServerProcessing.Log("new accept\n");
			} catch (IOException e) {e.printStackTrace();}
			logicServer.AddNewConnection(clientSocket);
			
			ServerProcessing.Log("got a connection\n");
		}
	}

	@Override
	public void run() {
		AcceptUsers();
	}
	

}
