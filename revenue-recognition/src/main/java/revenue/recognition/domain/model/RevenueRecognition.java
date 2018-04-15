package revenue.recognition.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Access(AccessType.FIELD)
@Table(name="revenueRecognitions")
public class RevenueRecognition {

	@EmbeddedId
	private RevenueRecognitionId revenueRecognitionId;

	@Transient
	private MonetaryAmount amount;

	public RevenueRecognition(
			Contract contract, LocalDate recognizedOn, MonetaryAmount amount) {
		if (contract == null) {
			throw new IllegalArgumentException("Contract cannot be null");
		}
		if (recognizedOn == null) {
			throw new IllegalArgumentException("Recognized date cannot be null");
		}
		if (amount == null) {
			throw new IllegalArgumentException("Amount cannot be null");
		}
		// this.contract = contract;
		// this.contractId = contract.getContractId().getValue();
		// this.recognizedOn = recognizedOn;
		this.revenueRecognitionId = new RevenueRecognitionId(
				contract.getContractId().getValue(), recognizedOn);
		this.amount = amount;
	}

	/*
	public RevenueRecognitionId getRevenueRecognitionId() {
		return revenueRecognitionId;
	}
	*/

	/*
	public Contract getContract() {
		return contract;
	}
	*/

	public LocalDate getRecognizedOn() {
		return revenueRecognitionId.getRecognizedOn();
	}

	public boolean isRecognizableBy(LocalDate asOf) {
		LocalDate recognizedOn = getRecognizedOn();
		return asOf.isAfter(recognizedOn)
				|| asOf.isEqual(recognizedOn);
	}

	public MonetaryAmount getAmount() {
		return amount;
	}

	@Transient
	private String currencyCode = Contract.CURRENCY.getCurrencyCode();

	@Column(name="amount", precision=10, scale=2)
	private BigDecimal amount_;

	@PrePersist
	protected void onPrePersist() {
		currencyCode = amount.getCurrency().getCurrencyCode();
		amount_ = amount.getNumber()
				.numberValue(BigDecimal.class)
				.setScale(amount.getCurrency().getDefaultFractionDigits(),
						RoundingMode.HALF_EVEN);
	}

	@PostLoad
	protected void onPostLoad() {
		this.amount =
				Monetary.getDefaultAmountFactory()
					.setNumber(amount_)
					.setCurrency(currencyCode)
					.create();
	}

	protected RevenueRecognition() { /* as needed by JPA/ORM */ }

}
