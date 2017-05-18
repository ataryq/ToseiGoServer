package IntegrationTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import code.DescProcessing;
import code.DescProcessing.Move;
import code.*;

public class JTestMove {

	private class SampleClient implements InterfaceClient {

		@Override
		public boolean IsRegistred() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String GetLogin() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void SetState(int _state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void SetGame(ProcessGame _game) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public ProcessGame GetGame() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void RefusedGame() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void SendMove(int X, int Y) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void SendRefusedMove() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void SendPass() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void SendAnswerMove(boolean accept) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean CheckConnection() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void PrepareDelete() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int GetState() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void Unconnected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void SendDiffMove(ArrayList<Move> moves) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void SendPoint(int point, int opp_point) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void SendEndGame() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean IsInvited() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void SetInvited(boolean _invited) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void CancelInvite() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void Invite(String name_inviter) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void StartGame(boolean color, int num_players) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Test
	public void test() {
		SampleClient white = new SampleClient();
		SampleClient black = new SampleClient();

		TestProgressGame game = new TestProgressGame(black, white, null);
		
		game.PutStone(2, 3, DescProcessing.BLACK);
		game.PutStone(2, 4, DescProcessing.BLACK);
		game.PutStone(2, 5, DescProcessing.BLACK);

		game.PutStone(3, 2, DescProcessing.BLACK);
		game.PutStone(3, 6, DescProcessing.BLACK);

		game.Move(4, 3, black);
		game.Move(3, 3, white);
		
		if(!game.GetSuccedLastMove()) {
			fail("Check to move is invalid");
		}
		game.Move(4, 4, black);
		if(!game.GetSuccedLastMove()) {
			fail("Check to move is invalid");
		}

		game.Move(3, 4, white);
		if(!game.GetSuccedLastMove()) {
			fail("Check to move is invalid");
		}

		game.Move(4, 6, black);
		if(!game.GetSuccedLastMove()) {
			fail("Check to move is invalid");
		}

		game.Move(3, 5, white);
		if(!game.GetSuccedLastMove()) {
			fail("Check to move is invalid");
		}

		game.Move(5, 5, black);
		if(!game.GetSuccedLastMove()) {
			fail("Check to move is invalid");
		}

		game.Move(4, 5, white);
		
		if(game.GetSuccedLastMove()) {
			fail("Check to move is invalid");
		}
		
	}

}
