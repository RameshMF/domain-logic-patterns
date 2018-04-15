package revenue.recognition.domain.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ContractId implements Serializable {

	private long id;
	
	public ContractId(long id) {
		this.id = id;
	}
	
	public long getValue() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		ContractId other = (ContractId) obj;
		if (id != other.id)
			return false;
		return true;
	}

	protected ContractId() { /* as needed by JPA/ORM */ }

}
