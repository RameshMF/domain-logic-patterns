package revenue.recognition.interfaces.facade;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import revenue.recognition.interfaces.facade.internal.DomainModelImpl;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DomainModelImplTests extends AbstractRevenueRecognitionServiceFacadeTests {

	@Autowired
	private DomainModelImpl serviceImpl;
	
	@Override
	protected RevenueRecognitionServiceFacade createRevenueRecognitionService() {
		return serviceImpl;
	}

}
