package code;
import java.net.Socket;

/**
 * Интерфейс логики сервера
 * @author ataryq
 *
 */
public interface InterfaceServerLogic {
	/**
	 * Добавить новое подключение
	 * @param new_user
	 */
	public void AddNewConnection(Socket new_user);
	/**
	 * Установить сокет сервер
	 * @param _base_server
	 */
	public void SetBaseServer(InterfaceServerBase _base_server);
	/**
	 * Запуск сервера
	 */
	public void Start();
	/**
	 * Попытка регистрации
	 * @param login логин
	 * @return true - пользователь успешно авторизван, false - иначе
	 */
	public boolean RegistredNewUser(String login);
	/**
	 * Получить список клиентов содержащих подстроку find_str, кроме самого вызывающего клинета
	 * @param _client вызывающтй клиент
	 * @param find_str искомая подстрока
	 * @return список логинов пользоватлей, находящихся в меню
	 */
	public String[] GetClients(InterfaceClient _client, String find_str);
	/**
	 * Поиск пользователя по логину
	 * @param login логин
	 * @return обьект пользователя
	 */
	public InterfaceClient FindPlayerByLogin(String login);
	/**
	 * Удаление клиента
	 * @param cl
	 */
	public void DeleteClient(InterfaceClient cl);
	/**
	 * Получить колличество всех игроков на сервере
	 * @return число игроков
	 */
	public int GetNumPlayers();
}
