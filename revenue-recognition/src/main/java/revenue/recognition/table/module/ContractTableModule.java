package revenue.recognition.table.module;

import static revenue.recognition.table.module.TableModuleImpl.CURRENCY;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import revenue.recognition.infrastructure.persistence.gateway.ContractTableDataGateway;

public class ContractTableModule {

	private final ContractTableDataGateway contractGateway;

	private final RevenueRecognitionTableModule revenueRecognitionTableModule;
	private final ProductTableModule productTableModule;

	public ContractTableModule(
			ContractTableDataGateway contractGateway,
			RevenueRecognitionTableModule rrTableModule,
			ProductTableModule productTableModule) {
		super();
		this.contractGateway = contractGateway;
		this.revenueRecognitionTableModule = rrTableModule;
		this.productTableModule = productTableModule;
	}

	public void calculateRecognitions(long contractId) {
		try (ResultSet contracts = contractGateway.findOne(contractId)) {
			if (! contracts.next()) {
				throw new RuntimeException(
						String.format(
								"Contract with id = [%d] not found", contractId));
			}
			BigDecimal totalRevenue = contracts.getBigDecimal("revenue");
			LocalDate dateSigned = contracts.getDate("dateSigned").toLocalDate();
			long productId = contracts.getLong("product_id");
			String type = productTableModule.getProductType(productId);
			if ("WORDPROCESSOR".equals(type)) {
				revenueRecognitionTableModule.insert(
						contractId, totalRevenue, dateSigned);
			} else if ("SPREADSHEET".equals(type)) {
				BigDecimal allocations[] = allocate(totalRevenue, 3);
				// recognize 1/3 today
				revenueRecognitionTableModule.insert(
						contractId, allocations[0], dateSigned);
				// recognize 1/3 after 60 days
				revenueRecognitionTableModule.insert(
						contractId, allocations[1], dateSigned.plusDays(60));
				// recognize 1/3 after 90 days
				revenueRecognitionTableModule.insert(
						contractId, allocations[2], dateSigned.plusDays(90));
			} else if ("DATABASE".equals(type)) {
				BigDecimal allocations[] = allocate(totalRevenue, 3);
				// recognize 1/3 today
				revenueRecognitionTableModule.insert(
						contractId, allocations[0], dateSigned);
				// recognize 1/3 after 30 days
				revenueRecognitionTableModule.insert(
						contractId, allocations[1], dateSigned.plusDays(30));
				// recognize 1/3 after 60 days
				revenueRecognitionTableModule.insert(
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
		int defaultFractionDigits =
				CURRENCY.getDefaultFractionDigits();
		BigDecimal onePart =
				totalRevenue.divide(new BigDecimal(count), RoundingMode.HALF_EVEN)
					.setScale(defaultFractionDigits, RoundingMode.HALF_EVEN);
		BigDecimal allocations[] = new BigDecimal[count];
		BigDecimal newTotal = new BigDecimal(0).setScale(
				defaultFractionDigits, RoundingMode.HALF_EVEN);
		for (int i = 0; i < count; i++) {
			allocations[i] = onePart;
			newTotal = newTotal.add(onePart);
		}
		if (! newTotal.equals(totalRevenue)) {
			// Adjust last allocation to equal total
			BigDecimal lastAllocation = allocations[count - 1];
			lastAllocation = lastAllocation.add(totalRevenue.subtract(newTotal));
			allocations[count - 1] = lastAllocation;
		}
		return allocations;
	}

	public long insert(
			long productId, BigDecimal contractPrice, LocalDate dateSigned) {
		return contractGateway.insert(productId, contractPrice, dateSigned);
	}

}
