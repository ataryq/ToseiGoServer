import java.util.ArrayList;

/**
 * ќбьедин€ет работу сервера, игр, клиентов
 * @author ataryq
 *
 */
public class Controller {
	//сервер
	InterfaceServerLogic serv;
	//список игр
	ArrayList<ProcessGame> games;
	
	public Controller() {
		games = new ArrayList<ProcessGame>();
	}
	
	/**
	 * ¬озвратит список клиентов без вызывающего клинта, содержащих указанную строку
	 * @param _client клиент который запрашивает список игроков
	 * @param find_str строка поиска 
	 * @return список клиентов
	 */
	public String[] GetClients(InterfaceClient _client, String find_str) {
		return serv.GetClients(_client, find_str);
	}
	
	/**
	 * инициализаци€
	 * @param _serv сервер
	 */
	public void Init(InterfaceServerLogic _serv) {
		serv = _serv;
	}
	
	/**
	 * ѕопытка авторизации
	 * @param login логин
	 * @param pl обьект игрок
	 * @return true - если авторизаци€ прошло успешно, иначе false
	 */
	public boolean RegistredNewUser(String login, InterfaceClient pl) {
		if( serv.RegistredNewUser(login) ) {
			ServerProcessing.Log("go to state IN_MENU \n");
			pl.SetState(InterfaceClient.IN_MENU);
			
			return true;
		} else return false;
	}
	
	/**
	 * ѕриглашение игрока
	 * @param host обьект хоста
	 * @param login логин приглащаемого пользовател€
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
	 * ѕолучить колличество пользователей
	 * @return колличество авторизованных пользователей
	 */
	public int GetNumPlayers() {
		return serv.GetNumPlayers();
	}
	
	/**
	 * ”далиние пользовател€
	 * @param cl обьект пользовател€
	 */
	public void Unconnect(InterfaceClient cl) {
		serv.DeleteClient(cl);
		
	}
	
	/**
	 * ќкончание партии
	 * @param _game обьект партии
	 */
	public void EndGame(ProcessGame _game) {
		_game.EndGame();
		DeleteGame(_game);
	}
	
	/**
	 * ”далить партию
	 * @param _game обьект партии
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
	 * ќтменить запрос на приглашение
	 * @param cl приглашающий пользователь
	 */
	public void CancelRequestInvite(InterfaceClient cl) {
		cl.GetGame().CancelInvite();
		DeleteGame(cl.GetGame());
	}
	
	/**
	 * ќтвет на приглашение игры
	 * @param responce true - прин€тие прглашени€, false - отказ
	 * @param cl приглашаемый пользователь
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
