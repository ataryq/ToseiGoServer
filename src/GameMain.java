import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class GameMain {
	//public static String adress = "188.227.19.61";
	public static String adress = "";
	public static int port_secure = 2500;
	public static int port = 6000;


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerProcessing.Init();
		
		//ReadFileSettings("config.txt");
		ServerProcessing.Log("start main \n");
		Controller contrl = new Controller();
		InterfaceServerLogic serv_logic = new ServerProcessing(contrl);
		InterfaceServerBase ser = new SocketServer(serv_logic);
		serv_logic.SetBaseServer(ser);
		serv_logic.Start();
		contrl.Init(serv_logic);
		
		//DescProcessing desc = new DescProcessing(19);
		//desc.Test();
		
		StartSecurityServer();
		
		
	}
	
    static DataOutputStream out;
    static DataInputStream in;
    static byte[] policyRequest = new byte[23];
    static byte[] xmlBytes;
    static int xmlBytesCount = 0;
	
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
	
	public static String ReadFileSettings(String fileName)  {
	    //Этот спец. объект для построения строки
	    StringBuilder sb = new StringBuilder();
	 
	    File file = new File(fileName);
	    if(!file.exists()){
            try {
				file.createNewFile();
				ServerProcessing.Log("create config file: " + file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
				ServerProcessing.Log(e.getMessage());
				ServerProcessing.FlushLog();
			}
        }
	 
	    try {
	        //Объект для чтения файла в буфер
	        @SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
	        GameMain.adress = in.readLine();
	    	ServerProcessing.Log("reading ip from file: " + GameMain.adress);

	    } catch(IOException e) {
	    	e.printStackTrace();
	    	ServerProcessing.Log(e.getMessage());
	    	ServerProcessing.FlushLog();
	    }
	 
	    //Возвращаем полученный текст с файла
	    return sb.toString();
	}
	
}
