import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Сокет сервер
 * Отвечает за низшую работу сокетов: принимате, отправляет сообщения адресату
 * @author ataryq
 */
public class SocketClient implements Runnable {
	protected InterfaceAction client;
	protected Socket sock;
	protected BufferedReader reader;
	protected InputStreamReader reader_in;
	private PrintWriter writer;
	private boolean end_connect = false;
	
	/**
	 * Конструктор
	 * @param _sock сокет
	 * @param _client обьект клиента
	 */
	SocketClient(Socket _sock, Client _client) {
		client = _client;
		sock = _sock;
		try {
			reader_in = new InputStreamReader(sock.getInputStream());
			writer = new PrintWriter(_sock.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		reader = new BufferedReader(reader_in);
	}
	
	/**
	 * Отправляет сообщение на обработку логике Client
	 * @param msg
	 */
	protected void ProcessMessage(String msg) {
		if(msg.isEmpty()) return;
		
		client.Action(msg);
	}
	
	/**
	 * Удаление сокета клинета
	 */
	public void PrepareDelete() {
		try {
			sock.close();
			end_connect = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * отправить сообщение
	 * @param msg сообщение
	 */
	public void SendMessage(String msg) {
		msg += "\0";
		writer.write(msg);
		writer.flush();
	}
	
	/**
	 * Отправить логике, что соединение оборвано
	 */
	public void SendUnconnected() {
		ProcessMessage("-1");
	}
	
	@Override
	/**
	 * Принятие сообщений от пользователя
	 */
	public void run() {
		String message;
		try {
			while ( !end_connect && ( message = reader.readLine()) != null ) {
				ServerProcessing.Log("msg: /" + message + "/\n");
								
				ProcessMessage(message);
			}
			ServerProcessing.Log("client unconnected!\n");
			ServerProcessing.FlushLog();
			SendUnconnected();
		} catch (Exception ex) {
			ServerProcessing.Log("client unconnected!\n");
			ServerProcessing.FlushLog();
			SendUnconnected();
			ex.printStackTrace();
		}
	}

}
