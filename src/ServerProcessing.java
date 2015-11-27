import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ServerProcessing extends TimerTask implements InterfaceServerLogic {
	public InterfaceServerBase base_server;
	protected ArrayList<InterfaceClient> clients;
	protected Controller control;
	public static JTextArea log_errors;
	public static JTextArea log = null;
	private static File file;
	private static PrintWriter out;
	protected static Timer timer_flush_log = new Timer();

	public ServerProcessing(Controller _control)  {
		clients = new ArrayList<InterfaceClient>();
		control = _control;
					
		TimerTask task = new TaskFlushLog();
		timer_flush_log.schedule(task, 5000, 5000);
	}
	
	class TaskFlushLog extends TimerTask {
		@Override
		public void run() {
			
			ServerProcessing.FlushLog();
		}
	}
	
	public static void Init() {
		try {
			file = new File("log.txt");
			out = new PrintWriter(file.getAbsoluteFile());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			out = null;
			e.printStackTrace();
		}
	}
	
	public static void Log(String msg) {
		if(out != null) out.println(msg);
		if(log != null) log.append(msg);
		System.out.println(msg);
	}
	
	public static void FlushLog() {
		try {
			out.flush();
		} catch(Exception ex){ServerProcessing.Log(ex.getMessage() + "\n");}
		
	}
	
	public int GetNumPlayers() {
		return clients.size();
	}
	
	@Override
	public void AddNewConnection(Socket new_sock) {
		InterfaceClient new_client = new Client(new_sock, control);
		clients.add(new_client);
	}

	@Override
	public void SetBaseServer(InterfaceServerBase _base_server) {
		base_server = _base_server;
	}

	public void StartGraphicalPart() {
		JFrame frame = new JFrame("Server GO");
		log = new JTextArea();
		log.setText("Log: \n");
		frame.getContentPane().add(BorderLayout.CENTER, log);
		
		log_errors = new JTextArea();
		log_errors.setText("Errors log: \n");
		frame.getContentPane().add(BorderLayout.SOUTH, log_errors);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setVisible(true);
		ServerProcessing.Log("start graphic part \n");
	}

	public void Start() {
		StartGraphicalPart();
		base_server.Start();

	}

	@Override
	public boolean RegistredNewUser(String login) 
	{
		for(int i = 0; i < clients.size(); i++) 
		{
			InterfaceClient client = clients.get(i);
			if(!client.IsRegistred()) continue;
			if( login.equals( client.GetLogin() ) ) return false;
		}
		return true;
	}
	
	public String[] GetClients(InterfaceClient _client, String find_str) {
		ArrayList<String> login_list = new ArrayList<String>();
		InterfaceClient cl;
		for(int i = 0; i < clients.size(); i++) 
		{
			cl = clients.get(i);
			if(cl.IsRegistred() && 
					!cl.IsInvited() && 
					cl.GetState() == InterfaceClient.IN_MENU)
			{
				if(cl != _client && cl.GetLogin().indexOf(find_str) != -1) {
					login_list.add(cl.GetLogin());
				}
			}
		}
		
		String[] output = new String[login_list.size()];
		for (int i = 0; i != login_list.size(); i++) {
		    output[i] = login_list.get(i);
		}
		
		return output;
	}
	
	public InterfaceClient FindPlayerByLogin(String login) {
		for(int i = 0; i < clients.size(); i++) {
			if(login.equals(clients.get(i).GetLogin())) return clients.get(i);
		}
		return null;
	}

	public void DeleteClient(InterfaceClient cl) {
		int i = 0;
		for(; i < clients.size(); i++ ) {
			if(clients.get(i) == cl) {
				DeleteClient(i);
			}
		}
	}
	
	public void DeleteClient(int num_client) {
		clients.get(num_client).PrepareDelete();
		clients.remove(num_client);
	}
	
	/** 
	 * check breaks connection
	 */
	@Override
	public void run() {
		//System.out.println("check " + clients.size());
		for(int i = 0; i < clients.size(); i++) {
			if( !clients.get(i).CheckConnection() ) {
				ServerProcessing.Log("connection break with :" + clients.get(i).GetLogin() + "\n");
				DeleteClient(i);
			}
		}
	}
}
