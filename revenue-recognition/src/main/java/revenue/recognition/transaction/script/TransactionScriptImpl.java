package revenue.recognition.transaction.script;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;

import revenue.recognition.infrastructure.persistence.gateway.ContractTableDataGateway;
import revenue.recognition.infrastructure.persistence.gateway.ProductTableDataGateway;
import revenue.recognition.infrastructure.persistence.gateway.RevenueRecognitionTableDataGateway;
import revenue.recognition.interfaces.facade.RevenueRecognitionServiceFacade;

public class TransactionScriptImpl implements RevenueRecognitionServiceFacade {

	public static final CurrencyUnit CURRENCY = Monetary.getCurrency("USD");

	private final RevenueRecognitionTableDataGateway recognitionGateway;
	private final ProductTableDataGateway productGateway;
	private final ContractTableDataGateway contractGateway;
	private final MonetaryAmountFactory<?> amountFactory;

	public TransactionScriptImpl(
			RevenueRecognitionTableDataGateway recognitionGateway,
			ContractTableDataGateway contractGateway,
			ProductTableDataGateway productGateway) {
		super();
		this.recognitionGateway = recognitionGateway;
		this.productGateway = productGateway;
		this.contractGateway = contractGateway;
		this.amountFactory = Monetary.getDefaultAmountFactory();
	}

	@Override
	public MonetaryAmount recognizedRevenue(long contractId, LocalDate asOf) {
		BigDecimal total = BigDecimal.ZERO.setScale(
				CURRENCY.getDefaultFractionDigits(), RoundingMode.HALF_EVEN);
		try (ResultSet rs = recognitionGateway.findByContract(contractId, asOf)) {
			while (rs.next()) {
				total = total.add(rs.getBigDecimal("amount"));
			}
			return amountFactory.setNumber(total).setCurrency(CURRENCY).create();
		} catch (SQLException e) {
			// TODO Wrap as runtime application exception
			throw new RuntimeException(e);
		}
	}

	@Override
	public void calculateRevenueRecognitions(long contractId) {
		try (ResultSet contracts = contractGateway.findOne(contractId)) {
			if (! contracts.next()) {
				throw new RuntimeException(
						String.format(
								"Contract with id = [%d] not found", contractId));
			}
			BigDecimal totalRevenue = contracts.getBigDecimal("revenue");
			LocalDate dateSigned = contracts.getDate("dateSigned").toLocalDate();
			String type = contracts.getString("type");
			if ("WORDPROCESSOR".equals(type)) {
				recognitionGateway.insert(
						contractId, totalRevenue, dateSigned);
			} else if ("SPREADSHEET".equals(type)) {
				BigDecimal allocations[] = allocate(totalRevenue, 3);
				// recognize 1/3 today
				recognitionGateway.insert(
						contractId, allocations[0], dateSigned);
				// recognize 1/3 after 60 days
				recognitionGateway.insert(
						contractId, allocations[1], dateSigned.plusDays(60));
				// recognize 1/3 after 90 days
				recognitionGateway.insert(
						contractId, allocations[2], dateSigned.plusDays(90));
			} else if ("DATABASE".equals(type)) {
				BigDecimal allocations[] = allocate(totalRevenue, 3);
				// recognize 1/3 today
				recognitionGateway.insert(
						contractId, allocations[0], dateSigned);
				// recognize 1/3 after 30 days
				recognitionGateway.insert(
						contractId, allocations[1], dateSigned.plusDays(30));
				// recognize 1/3 after 60 days
				recognitionGateway.insert(
						contractId, allocations[2], dateSigned.plusDays(60));
			} else {
				throw new RuntimeException(
						String.format("Unknown product type [%s]", type));
			}
		} catch (SQLException e) {
			// TODO Wrap as runtime application exception
			throw new RuntimeException(e);
		}
	}

	private BigDecimal[] allocate(BigDecimal totalRevenue, int count) {
		int defaultFractionDigits = CURRENCY.getDefaultFractionDigits();
		BigDecimal onePart =
				totalRevenue.divide(new BigDecimal(count), RoundingMode.HALF_EVEN)
					.setScale(defaultFractionDigits, RoundingMode.HALF_EVEN);
		BigDecimal allocations[] = new BigDecimal[count];
		BigDecimal newTotal = BigDecimal.ZERO.setScale(
				defaultFractionDigits, RoundingMode.HALF_EVEN);
		for (int i = 0; i < count; i++) {
			allocations[i] = onePart;
			newTotal = newTotal.add(onePart);
		}
		if (! newTotal.equals(totalRevenue)) {
			// Adjust last allocation to achieve total revenue
			BigDecimal lastAllocation = allocations[count - 1];
			lastAllocation = lastAllocation.add(totalRevenue.subtract(newTotal));
			allocations[count - 1] = lastAllocation;
		}
		return allocations;
	}

	@Override
	public long insertContractInformation(
			long productId, MonetaryAmount contractPrice, LocalDate dateSigned) {
		return contractGateway.insert(
				productId,
				contractPrice.getNumber().numberValue(BigDecimal.class).setScale(
						contractPrice.getCurrency().getDefaultFractionDigits(),
						RoundingMode.HALF_EVEN),
				dateSigned);
	}

	@Override
	public long insertProductInformation(String name, String type) {
		return productGateway.insert(name, type);
	}
	
}
