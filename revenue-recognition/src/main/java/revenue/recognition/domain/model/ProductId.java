package revenue.recognition.domain.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductId implements Serializable {

	private long id;

	public ProductId(long id) {
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
		ProductId other = (ProductId) obj;
		if (id != other.id)
			return false;
		return true;
	}

	protected ProductId() { /* as needed by JPA/ORM */ }

}
