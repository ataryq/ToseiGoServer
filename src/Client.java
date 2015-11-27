import java.net.Socket;
import java.util.ArrayList;

public class Client  implements InterfaceClient, InterfaceAction {
	protected final String SPLIT_COM = "_";
	protected final String SPLIT_MSG = ";";
	
	protected SocketClient client;
	protected Controller control;
	protected String login;
	protected int cur_state = 0;
	protected ProcessGame game = null;
	protected boolean invited = false;
	
	private boolean is_registred = false;
	
	public boolean IsRegistred() {
		return is_registred;
	}
	
	public boolean IsInvited() {
		return invited;
	}
	
	public void SetInvited(boolean _invited) {
		invited = _invited;
	}
	
	public void SetRegistred(boolean _is_registred) {
		is_registred = _is_registred;
	}
	
	public String GetLogin() {
		return login;
	}
		
	public Client(Socket soc, Controller _control) 
	{
		client = new SocketClient(soc, this);
		control = _control;
		Thread t = new Thread(client);		// создание параллельеного потока
		t.start();		// запуск потока
		ServerProcessing.Log("got a connection\n");
		String num_pl = Integer.toString(control.GetNumPlayers());
		ServerProcessing.Log("num_pl: " + num_pl + "\n");

		client.SendMessage("0_0_server: connected to server" + SPLIT_MSG + num_pl);
	}

	@Override
	public void Action(String msg) 
	{
		//client unconnected
		if(msg.equals("-1")) {
			ServerProcessing.Log("Client: " + this.login + " unconnected \n");
			this.Unconnected();
		}
		
		switch(cur_state)
		{
		case AUTHORIZATE: {
			Authorizate(msg);
		} break;
		case IN_MENU: {
			InMenu(msg);
		} break;
		case PRECESSING_GAME: {
			ProcessGame(msg);
		} break;
		case END_GAME: {
			EndGame(msg);
		} break;
		default: {
			ServerProcessing.Log("Error message unrecognazed \n");
		}
		}
	}
	
	public void SetState(int _state) {
		//deactivate
		switch(cur_state)
		{
		case AUTHORIZATE: {
			
		} break;
		case IN_MENU: {
			if(cur_state != _state) invited = false;
		} break;
		case PRECESSING_GAME: {
			
		} break;
		case END_GAME: {
			
		} break;
		default: {
			ServerProcessing.Log("Error message unrecognazed \n");
		}
		}
		
		cur_state = _state;
	}
	
	protected String[] ParseMsg(String msg) {
		msg = msg.substring(msg.indexOf(SPLIT_COM) + 1);
		String[] msgs = msg.split(SPLIT_COM);
		return msgs;
	}
	
	protected String[] ParseParam(String msg) {
		String[] msgs = msg.split(SPLIT_MSG);
		return msgs;
	}
	
	protected void Authorizate(String msg) {
		ServerProcessing.Log("Authorizate \n");
		String[] msgs = ParseMsg(msg);
		
		if(msgs.length < 1) return;
		
		switch(msgs[0])
		{
		case "1": {
			if(msgs.length < 2) {
				ServerProcessing.Log("name not found \n");
				break;
			}
			
			SetState(cur_state);
			if(msgs[1].length() < 4) {
				client.SendMessage("1_1_2");
			} else {
				if( control.RegistredNewUser(msgs[1], this) ) {
					login = msgs[1];
					this.SetRegistred(true);
					String num_pl = Integer.toString(control.GetNumPlayers());
					ServerProcessing.Log("num_pl: " + num_pl + "\n");
					String send_msg = "1_1_1" + SPLIT_MSG + num_pl;

					client.SendMessage(send_msg);
					
					ServerProcessing.Log("user logined as: " + login + "\n");
				} else {
					//уведомить клиент о том, что логин уже занят
					client.SendMessage("1_1_0");
				}
			}
			
		}break;
		default: ServerProcessing.Log("Warning! Authorizate: Command not recognized \n");
		}
	}
	

	
	protected void InMenu(String msg) {
		ServerProcessing.Log("InMenu \n");

		String[] msgs = ParseMsg(msg);
		
		for(int i = 0; i < msgs.length; i++) {
			ServerProcessing.Log(msgs[i] + " ");
		}
		
		ServerProcessing.Log("\n");
		
		switch(msgs[0])
		{
		//запрос на получения списка игроков
		case "1": {
			String find_str = "";
			if(msgs.length > 1) {
				ServerProcessing.Log(msgs[1]);
				find_str = msgs[1];
			}
			String[] login_list = control.GetClients(this, find_str);
			String send_msg = "2_1_";
			
			String num_pl = Integer.toString(control.GetNumPlayers());
			ServerProcessing.Log("num_pl: " + num_pl + "\n");
			send_msg += num_pl + SPLIT_MSG;
			
			for(int i = 0; i < login_list.length; i++) {
				send_msg += login_list[i] + SPLIT_MSG;
			}			
			client.SendMessage(send_msg);
		} break;
		//выбор игрока для игры
		case "2": {
			String login = msgs[1];
			control.InvitePlayer(this, login);
		} break;
		//ответ на предложение игры
		case "3": {
			String responce = msgs[1];
			if(responce.equals("0")) control.ResponceInvite(false, this);
			else if(responce.equals("1")) control.ResponceInvite(true, this);
			else if(responce.equals("2")) control.CancelRequestInvite(this);
			else ServerProcessing.Log("error in parse command responce \n");
		} break;
		default: ServerProcessing.Log("Warning! InMenu: Command not recognized\n");
		}
		
	}
	
	public void Invite(String name_inviter) {
		invited = true;
		SendInvite(name_inviter);
	}
	
	public boolean SendInvite(String inviter) {
		ServerProcessing.Log("send invite game \n");
		client.SendMessage("2" + SPLIT_COM + "2" + SPLIT_COM + inviter);
		return true;
	}
	
	public void StartGame() {
		invited = false;
		ServerProcessing.Log("send start game \n");
		client.SendMessage("2" + SPLIT_COM + "3" + SPLIT_COM + "1");
	}
	
	public void RefusedGame() {
		invited = false;
		ServerProcessing.Log("send refused game \n");
		client.SendMessage("2" + SPLIT_COM + "3" + SPLIT_COM + "0");
	}
	
	public void CancelInvite() {
		invited = false;
		ServerProcessing.Log("send cancel invite \n");
		client.SendMessage("2" + SPLIT_COM + "3" + SPLIT_COM + "2" + SPLIT_MSG + "2");
	}

	
	protected void ProcessGame(String msg) {
		ServerProcessing.Log("ProcessGame \n");

		try {
			String[] msgs = ParseMsg(msg);
			for(int i = 0; i < msgs.length; i++) {
				ServerProcessing.Log(msgs[i] + " \n");
			}
			switch(msgs[0])
			{
			//send move
			case "1": {
				String [] params = ParseParam(msgs[1]);
				
				game.Move(new Integer(params[0]), new Integer(params[1]), this);
			} break;
			//pass
			case "2": {
				game.Pass(this);
			} break;
			default: {
				ServerProcessing.Log("Warning! ProcessGame: Command not recognized \n");
			}
			}
		} catch(Exception ex){
			ex.printStackTrace();
			ServerProcessing.log_errors.append("Error! ProcessGame \n");
		}
		
	}
	
	public void EndGame() {
		ServerProcessing.Log("end game \n");
		client.SendMessage("3" + SPLIT_COM + "3");
	}
	
	public void SendMove(int X, int Y) {
		ServerProcessing.Log("send move \n");
		client.SendMessage("3" + SPLIT_COM + "1" + SPLIT_COM + 
				Integer.toString(X) + SPLIT_MSG + Integer.toString(Y));
	}
	
	public void SendRefusedMove() {
		ServerProcessing.Log("refused move \n");
		client.SendMessage("3" + SPLIT_COM + "4");
	}
	public void SendPass() {
		ServerProcessing.Log("send pass \n");
		client.SendMessage("3" + SPLIT_COM + "2");
	}
	
	public void SendEndGame() {
		ServerProcessing.Log("send end game \n");
		client.SendMessage("3" + SPLIT_COM + "3");

	}
	
	protected void EndGame(String msg) {
		ServerProcessing.Log("EndGame \n");
		
	}
	
	public void SetGame(ProcessGame _game) {
		game = _game;
	}
	
	public ProcessGame GetGame() {
		return game;
	}
	
	public void SendAnswerMove(boolean accept) {
		ServerProcessing.Log("send answer move \n");
		if(accept) {
			client.SendMessage("3" + SPLIT_COM + "5" + SPLIT_COM + "1");
			ServerProcessing.Log("good \n");
		} else {
			client.SendMessage("3" + SPLIT_COM + "5" + SPLIT_COM + "0");

			ServerProcessing.Log("bad \n");
		}
	}

	@Override
	public void StartGame(boolean color, int num_players) {
		// TODO Auto-generated method stub
		if(color) client.SendMessage("2" + SPLIT_COM + "3" + SPLIT_COM + "1" + SPLIT_MSG + "1" + 
				SPLIT_MSG + Integer.toString(num_players));
		else client.SendMessage("2" + SPLIT_COM + "3" + SPLIT_COM + "1" + SPLIT_MSG + "0");
	}

	@Override
	public boolean CheckConnection() {
		// TODO Auto-generated method stub
		
		return true;
	}

	@Override
	public void PrepareDelete() {
		// TODO Auto-generated method stub
		if(game != null) game.EndGame();
		client.PrepareDelete();
	}

	@Override
	public int GetState() {
		// TODO Auto-generated method stub
		return cur_state;
	}

	@Override
	public void Unconnected() {
		// TODO Auto-generated method stub
		control.Unconnect(this);
	}

	@Override
	public void SendDiffMove(ArrayList<Move> moves) {
		// TODO Auto-generated method stub
		String msg = "3" + SPLIT_COM + "6" + SPLIT_COM;
		for(int i = 0; i < moves.size(); i++) {
			msg += moves.get(i).x + SPLIT_MSG;
			msg += moves.get(i).y + SPLIT_MSG;
			msg += moves.get(i).type + SPLIT_MSG;
		}
		client.SendMessage(msg);
	}

	@Override
	public void SendPoint(int point, int opp_point) {
		// TODO Auto-generated method stub
		client.SendMessage("3" + SPLIT_COM + "7" + SPLIT_COM + Integer.toString(point) +
				SPLIT_MSG + Integer.toString(opp_point));
	}
	
	
}