import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main class
 * @author ataryq
 */
public class GameMain {
	public static String adress = "";
	public static int port_secure = 2500;
	public static int port = 6000;

	/**
	 * main class
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerProcessing.Init();
		
		ServerProcessing.Log("start main \n");
		//create controller
		Controller contrl = new Controller();
		//create server
		InterfaceServerLogic serv_logic = new ServerProcessing(contrl);
		InterfaceServerBase ser = new SocketServer(serv_logic);
		serv_logic.SetBaseServer(ser);
		serv_logic.Start();
		contrl.Init(serv_logic);
		
		// start security server, needed for flash apps
		StartSecurityServer();
	}
	
	/**
	 * ѕосылает содержание crossdomain.xml в ответ на подключение к этому порту
	 * Ѕез этого сервера не будет работать только флеш приложение
	 */
    static DataOutputStream out;
    static DataInputStream in;
    static byte[] policyRequest = new byte[23];
    static byte[] xmlBytes;
    static int xmlBytesCount = 0;
	
    /**
     * запуск сервера
     */
	private static void StartSecurityServer() {
		ServerProcessing.Log("-> Security server started... \n");
         ServerSocket server;
		try {
	         Socket client;
	         
	         // Reading crossdomain.xml
	         FileInputStream xmlFile = null;
			try {
				xmlFile = new FileInputStream("files/crossdomain.xml");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         xmlBytesCount = xmlFile.available();
	         
	         xmlBytes = new byte[xmlBytesCount + 1];
	         xmlFile.read(xmlBytes, 0, xmlBytesCount);
	         xmlBytes[xmlBytesCount] = 0;
	         xmlFile.close();
	         
	         
			 server = new ServerSocket(GameMain.port_secure);
			 ServerProcessing.Log("-> Security server started normal \n");
	         for (;;)
	         {
	             client = server.accept();
	             SendPolicy(client, true);
	             client.close();
	         }
	         
		} catch(Exception ex) {
			ServerProcessing.Log(ex.getMessage());
	    	ServerProcessing.FlushLog();
			ex.printStackTrace();
		}
	}
	
	/**
	 * посылает файл безопасности
	 * @param _client  сокет клиент
	 * @param wait_reading  был ли считан запрос на файл безопасности
	 */
	public static void SendPolicy(Socket _client, boolean wait_reading) {
		try {
			out = new DataOutputStream(_client.getOutputStream());
	        in = new DataInputStream(_client.getInputStream());
	        
	        if(wait_reading) in.read(policyRequest, 0, 23);
	        
	        out.write(xmlBytes, 0, xmlBytesCount + 1);
	        out.flush();
	        System.out.println(":: FSS :: Policy sended to " + _client.toString());

		} catch(Exception ex) {
			ex.printStackTrace();
			ServerProcessing.Log(ex.getMessage() + "\n");
		}
	}
	
}
