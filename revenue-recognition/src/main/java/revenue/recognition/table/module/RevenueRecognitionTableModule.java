package revenue.recognition.table.module;

import static revenue.recognition.table.module.TableModuleImpl.CURRENCY;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;

import revenue.recognition.infrastructure.persistence.gateway.RevenueRecognitionTableDataGateway;

public class RevenueRecognitionTableModule {

	private final RevenueRecognitionTableDataGateway recognitionGateway;
	private final MonetaryAmountFactory<?> amountFactory;

	public RevenueRecognitionTableModule(
			RevenueRecognitionTableDataGateway recognitionGateway) {
		this.recognitionGateway = recognitionGateway;
		this.amountFactory = Monetary.getDefaultAmountFactory();
	}

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

	public void insert(
			long contractId, BigDecimal amount, LocalDate recognizedOn) {
		recognitionGateway.insert(contractId, amount, recognizedOn);
	}

}
