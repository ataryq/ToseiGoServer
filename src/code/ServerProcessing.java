package code;
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

/**
 * Сервер
 * Логическая часть сервера, содержит в себе логику работы с сервером
 * @author ataryq
 *
 */
public class ServerProcessing extends TimerTask implements InterfaceServerLogic {
	public InterfaceServerBase base_server;
	protected ArrayList<InterfaceClient> clients;
	protected Controller control;
	public static JTextArea log_errors;
	public static JTextArea log = null;
	private static File file;
	private static PrintWriter out;
	protected static Timer timer_flush_log = new Timer();

	/**
	 * Конструктор
	 * @param _control контроллер
	 */
	public ServerProcessing(Controller _control)  {
		clients = new ArrayList<InterfaceClient>();
		control = _control;
					
		TimerTask task = new TaskFlushLog();
		timer_flush_log.schedule(task, 5000, 5000);
	}
	
	/**
	 * Таймер, переодически запрашивающий запись информации в файл
	 * @author ataryq
	 *
	 */
	class TaskFlushLog extends TimerTask {
		@Override
		public void run() {
			
			ServerProcessing.FlushLog();
		}
	}
	
	/**
	 * Инициализация сервера
	 */
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
	
	/**
	 * Запись лога сервера
	 * @param msg сообщение лога
	 */
	public static void Log(String msg) {
		if(out != null) out.println(msg);
		if(log != null) log.append(msg);
		System.out.println(msg);
	}
	
	/**
	 * Запист непосредственно на диск
	 */
	public static void FlushLog() {
		try {
			out.flush();
		} catch(Exception ex){ServerProcessing.Log(ex.getMessage() + "\n");}
		
	}
	
	/**
	 * вернет колличество игроков
	 */
	public int GetNumPlayers() {
		return clients.size();
	}
	
	/**
	 * Добавить новое подключение
	 * @param new_sock сокет пользователя
	 */
	public void AddNewConnection(Socket new_sock) {
		InterfaceClient new_client = new Client(new_sock, control);
		clients.add(new_client);
	}

	/**
	 * Задать сокет сервер
	 * @param _base_server сокет серер
	 */
	@Override
	public void SetBaseServer(InterfaceServerBase _base_server) {
		base_server = _base_server;
	}

	/**
	 * Старт графиской части сервера
	 */
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

	/**
	 * Запуск сервера
	 */
	public void Start() {
		StartGraphicalPart();
		base_server.Start();

	}

	@Override
	/**
	 * Авторизация нового пользователя
	 * @param login
	 * @return true - успешная регистрация, false  иначе
	 */
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
	
	/**
	 * @param Список игрков _client
	 * @param Строка поиска find_str
	 * @return Список игрков
	 */
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
	
	/**
	 * Найти игрока по логину
	 * @param login Логин игрока
	 * @return обьект ирока
	 */
	public InterfaceClient FindPlayerByLogin(String login) {
		for(int i = 0; i < clients.size(); i++) {
			if(login.equals(clients.get(i).GetLogin())) return clients.get(i);
		}
		return null;
	}

	/**
	 * удаление игрока
	 * @param cl обьект игрока
	 */
	public void DeleteClient(InterfaceClient cl) {
		int i = 0;
		for(; i < clients.size(); i++ ) {
			if(clients.get(i) == cl) {
				DeleteClient(i);
			}
		}
	}
	
	/**
	 * удаление игрока по номеру
	 * @param num_client номер игрока
	 */
	public void DeleteClient(int num_client) {
		clients.get(num_client).PrepareDelete();
		clients.remove(num_client);
	}
	
	/** 
	 * Проверка обрыва соединения
	 * сейчас не используется
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
