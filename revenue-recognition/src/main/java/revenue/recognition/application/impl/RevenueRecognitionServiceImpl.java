package revenue.recognition.application.impl;

import java.time.LocalDate;

import javax.money.MonetaryAmount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import revenue.recognition.application.RevenueRecognitionService;
import revenue.recognition.domain.model.Contract;
import revenue.recognition.domain.model.ContractId;
import revenue.recognition.domain.model.ContractRepository;

@Transactional
@Service
public class RevenueRecognitionServiceImpl implements RevenueRecognitionService {

	private final ContractRepository contractRepository;

	@Autowired
	public RevenueRecognitionServiceImpl(
			ContractRepository contractRepository) {
		this.contractRepository = contractRepository;
	}

	@Override
	public MonetaryAmount recognizedRevenue(ContractId contractId, LocalDate asOf) {
		Contract contract = contractRepository.findOne(contractId);
		return contract.recognizedRevenue(asOf);
	}

	@Override
	public void calculateRevenueRecognitions(ContractId contractId) {
		Contract contract = contractRepository.findOne(contractId);
		contract.calculateRevenueRecognitions();
		// No need to call EntityManager#persist(Object)
		// or EntityManager#merge(Object)
		// since the contract entity is *managed*.
		// contractRepository.save(contract);
	}

}
