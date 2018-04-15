package revenue.recognition.interfaces.facade.internal;

import java.time.LocalDate;

import javax.money.MonetaryAmount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import revenue.recognition.application.RevenueRecognitionService;
import revenue.recognition.domain.model.Contract;
import revenue.recognition.domain.model.ContractId;
import revenue.recognition.domain.model.ContractRepository;
import revenue.recognition.domain.model.Product;
import revenue.recognition.domain.model.ProductId;
import revenue.recognition.domain.model.ProductRepository;
import revenue.recognition.interfaces.facade.RevenueRecognitionServiceFacade;

@Service
public class DomainModelImpl implements RevenueRecognitionServiceFacade {

	private RevenueRecognitionService recognitionService;
	private ContractRepository contractRepository;
	private ProductRepository productRepository;

	@Autowired
	public DomainModelImpl(
			RevenueRecognitionService recognitionService,
			ContractRepository contractRepository,
			ProductRepository productRepository) {
		super();
		this.recognitionService = recognitionService;
		this.contractRepository = contractRepository;
		this.productRepository = productRepository;
	}

	@Override
	public MonetaryAmount recognizedRevenue(long contractId, LocalDate asOf) {
		return recognitionService.recognizedRevenue(
				new ContractId(contractId), asOf);
	}

	@Override
	public void calculateRevenueRecognitions(long contractId) {
		recognitionService.calculateRevenueRecognitions(new ContractId(contractId));
	}

	@Override
	public long insertContractInformation(
			long productId, MonetaryAmount contractPrice, LocalDate dateSigned) {
		Product product = productRepository.findOne(new ProductId(productId));
		Contract contract = new Contract(product, contractPrice, dateSigned);
		contractRepository.save(contract);
		return contract.getContractId().getValue();
	}

	@Override
	public long insertProductInformation(String name, String type) {
		Product product = new Product(name, type);
		productRepository.save(product);
		return product.getProductId().getValue();
	}

}
