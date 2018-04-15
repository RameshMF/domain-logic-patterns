package revenue.recognition.infrastructure.persistence.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import revenue.recognition.domain.model.Product;
import revenue.recognition.domain.model.ProductId;
import revenue.recognition.domain.model.ProductRepository;

@Transactional
@Repository
public class JpaProductRepository
		extends JpaGenericRepository<Product, ProductId>
		implements ProductRepository {

	public JpaProductRepository() {
		super(Product.class);
	}

	// Override some methods, since ProductId is NOT really the PK

	@Override
	public Product findOne(ProductId id) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Id must not be null");
		}
		return getEntityManager().find(
				getEntityClass(), id.getValue());
	}

	@Override
	public void delete(ProductId id) {
		Product entity = getEntityManager().find(
				getEntityClass(), id.getValue());
		delete(entity);
	}

}
