package revenue.recognition.interfaces.facade;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import revenue.recognition.transaction.script.TransactionScriptImpl;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionScriptImplTests extends AbstractRevenueRecognitionServiceFacadeTests {

	@Autowired
	private TransactionScriptImpl serviceImpl;

	@Override
	protected RevenueRecognitionServiceFacade createRevenueRecognitionService() {
		return serviceImpl;
	}

}
