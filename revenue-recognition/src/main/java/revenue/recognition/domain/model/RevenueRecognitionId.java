package revenue.recognition.domain.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@SuppressWarnings("serial")
public class RevenueRecognitionId implements Serializable {

	@Column(name="contract_id", nullable=false)
	private Long contractId;
	@Column(name="recognizedOn", nullable=false)
	private LocalDate recognizedOn;

	public RevenueRecognitionId(Long contractId, LocalDate recognizedOn) {
		super();
		this.contractId = contractId;
		this.recognizedOn = recognizedOn;
	}

	public Long getContractId() {
		return contractId;
	}

	public LocalDate getRecognizedOn() {
		return recognizedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
		result = prime * result + ((recognizedOn == null) ? 0 : recognizedOn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RevenueRecognitionId other = (RevenueRecognitionId) obj;
		if (contractId == null) {
			if (other.contractId != null)
				return false;
		} else if (!contractId.equals(other.contractId))
			return false;
		if (recognizedOn == null) {
			if (other.recognizedOn != null)
				return false;
		} else if (!recognizedOn.equals(other.recognizedOn))
			return false;
		return true;
	}

	protected RevenueRecognitionId() { /* as needed by JPA/ORM */ }

}
