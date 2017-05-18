package HardTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ JTestCheckClosed.class, JTestCheckDeathless.class, JTestCheckNotSuicide.class, JTestCheckSuicide.class,
		JTestDeleteSurroundStone.class })
public class AllTests {

}
