package revenue.recognition.table.module;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import revenue.recognition.interfaces.facade.RevenueRecognitionServiceFacade;

public class TableModuleImpl implements RevenueRecognitionServiceFacade {

	public static final CurrencyUnit CURRENCY = Monetary.getCurrency("USD");

	private final RevenueRecognitionTableModule revenueRecognitions;
	private final ContractTableModule contracts;
	private final ProductTableModule products;

	public TableModuleImpl(
			RevenueRecognitionTableModule revenueRecognitions,
			ContractTableModule contracts,
			ProductTableModule products) {
		super();
		this.revenueRecognitions = revenueRecognitions;
		this.contracts = contracts;
		this.products = products;
	}

	@Override
	public MonetaryAmount recognizedRevenue(long contractId, LocalDate asOf) {
		return revenueRecognitions.recognizedRevenue(contractId, asOf);
	}

	@Override
	public void calculateRevenueRecognitions(long contractId) {
		contracts.calculateRecognitions(contractId);
	}

	@Override
	public long insertContractInformation(
			long productId, MonetaryAmount contractPrice, LocalDate dateSigned) {
		return contracts.insert(
				productId,
				contractPrice.getNumber().numberValue(BigDecimal.class).setScale(
						contractPrice.getCurrency().getDefaultFractionDigits(),
						RoundingMode.HALF_EVEN),
				dateSigned);
	}

	@Override
	public long insertProductInformation(String name, String type) {
		return products.insert(name, type);
	}

}
