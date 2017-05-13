package code;
import java.util.ArrayList;

/**
 * Интрфейс логики клиента
 * @author ataryq
 *
 */
public interface InterfaceClient{
	public final int AUTHORIZATE = 0;
	public final int IN_MENU = 1;
	public final int PRECESSING_GAME = 2;
	public final int END_GAME = 3;
	
	/**
	 * @return true - пользователь авторизован, false - иначе
	 */
	public boolean IsRegistred();
	public String GetLogin();

	/**
	 * Установить состояние клиента
	 * @param _state номер состояния
	 */
	public void SetState(int _state);
	/**
	 * Передать ссылку на игру пользователю
	 * @param _game
	 */
	public void SetGame(ProcessGame _game);
	/**
	 * Получить ссылку на игру
	 * @return ссылка на игру
	 */
	public ProcessGame GetGame();
	/**
	 * Отказ на приглашение играть
	 */
	public void RefusedGame();
	/**
	 * Отправить ход противника
	 */
	public void SendMove(int X, int Y);
	/**
	 * Отправить, что ход невозможен
	 */
	public void SendRefusedMove();
	/**
	 * Отправит пасс
	 */
	public void SendPass();
	/**
	 * Отправить ответ о корректности ходе
	 * @param accept true - ход одобрен, false - иначе
	 */
	public void SendAnswerMove(boolean accept);
	/**
	 * Проверка соединения
	 * Сейчас не используется, для будущих фич
	 */
	public boolean CheckConnection();
	/**
	 * Подготовка к удаления(закрытие соединений, удаления обьектов)
	 */
	public void PrepareDelete();
	/**
	 * Вернуть текущее состояние клиента
	 * @return состояние клиента
	 */
	public int GetState();
	/**
	 * Соединение прервалось
	 */
	public void Unconnected();
	/**
	 * Послать разницу в камнях на доске
	 * @param moves
	 */
	public void SendDiffMove(ArrayList<DescProcessing.Move> moves);
	/**
	 * Послать очки
	 * @param point очки игрока
	 * @param opp_point очки оппонента
	 */
	public void SendPoint(int point, int opp_point);
	/**
	 * Сообщить о завершении игры
	 */
	public void SendEndGame();
	/**
	 * Приглашен ли пользователь
	 * @return true - если приглашен, false - инчае
	 */
	public boolean IsInvited();
	/**
	 * Установить приглашен ли пользователь
	 * @param _invited true - если приглашен, false - инчае
	 */
	public void SetInvited(boolean _invited);
	/**
	 * Отменить приглашение
	 */
	public void CancelInvite();
	
	/**
	 * Пригласить пользователя
	 * @param name_inviter логин пользователя
	 */
	public void Invite(String name_inviter);
	/**
	 * Начать игру
	 * @param color цвет камней игрока
	 * @param num_players колличество игроков
	 */
	public void StartGame(boolean color, int num_players);
}
