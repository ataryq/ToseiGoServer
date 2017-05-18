package IntegrationTests;

import code.Controller;
import code.DescProcessing;
import code.InterfaceClient;
import code.ProcessGame;

public class TestProgressGame extends ProcessGame {
	public TestProgressGame(InterfaceClient _host, 
			InterfaceClient _pl2,
			Controller _control) {
		super(_host, _pl2, _control);
	}
	
	public TestProgressGame() {
	}
	
	public boolean TestCheckMove(int x, int y, boolean type) {
		return this.CheckMove(x, y, type);
	}
	
	public void PutStone(int x, int y, char type)  {
		this.desc_proc.PutStone(x, y, type);
	}
	
	public DescProcessing GetDesc() {
		return this.desc_proc;
	}
};
