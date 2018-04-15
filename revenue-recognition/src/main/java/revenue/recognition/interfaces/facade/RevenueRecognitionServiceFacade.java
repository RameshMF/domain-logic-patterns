package revenue.recognition.interfaces.facade;

import java.time.LocalDate;

import javax.money.MonetaryAmount;

public interface RevenueRecognitionServiceFacade {

	MonetaryAmount recognizedRevenue(long contractId, LocalDate asOf);
	
	void calculateRevenueRecognitions(long contractId);
	
	long insertContractInformation(
			long productId, MonetaryAmount contractPrice, LocalDate dateSigned);

	long insertProductInformation(String name, String type);

}
