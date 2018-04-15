package revenue.recognition.infrastructure.persistence.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import revenue.recognition.domain.model.Contract;
import revenue.recognition.domain.model.ContractId;
import revenue.recognition.domain.model.ContractRepository;

@Transactional
@Repository
public class JpaContractRepository
		extends JpaGenericRepository<Contract, ContractId>
		implements ContractRepository {

	public JpaContractRepository() {
		super(Contract.class);
	}

	// Override some methods, since ContractId is NOT really the PK

	@Override
	public Contract findOne(ContractId id) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Id must not be null");
		}
		return getEntityManager().find(
				getEntityClass(), id.getValue());
	}

	@Override
	public void delete(ContractId id) {
		Contract entity = getEntityManager().find(
				getEntityClass(), id.getValue());
		delete(entity);
	}

}
