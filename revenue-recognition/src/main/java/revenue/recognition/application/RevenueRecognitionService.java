package revenue.recognition.application;

import java.time.LocalDate;

import javax.money.MonetaryAmount;

import revenue.recognition.domain.model.ContractId;

public interface RevenueRecognitionService {

	MonetaryAmount recognizedRevenue(ContractId contractId, LocalDate asOf);
	
	void calculateRevenueRecognitions(ContractId contractId);

}
