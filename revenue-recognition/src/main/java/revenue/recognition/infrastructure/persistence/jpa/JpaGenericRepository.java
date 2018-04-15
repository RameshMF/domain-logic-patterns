package revenue.recognition.infrastructure.persistence.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import revenue.recognition.domain.model.GenericRepository;;

@Transactional
public class JpaGenericRepository<T, ID extends Serializable> implements GenericRepository<T, ID> {

	@PersistenceContext
	private EntityManager em;

	private final Class<T> entityClass;

	public JpaGenericRepository(Class<T> entityClass) {
		if (entityClass == null) {
			throw new IllegalArgumentException("Entity class cannot be null");
		}
		this.entityClass = entityClass;
	}

	protected EntityManager getEntityManager() {
		return em;
	}

	// For testing-purposes only
	void setEntityManager(EntityManager em) {
		this.em = em;
	}

	protected Class<T> getEntityClass() {
		return entityClass;
	}

	protected String getEntityName() {
		return em.getMetamodel().entity(getEntityClass()).getName();
	}

	@Override
	public T findOne(ID id) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Id must not be null");
		}
		return em.find(getEntityClass(), id);
	}

	@Override
	public T save(T entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public T update(T entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(ID id) {
		T entity = em.find(getEntityClass(), id);
		delete(entity);
	}

	@Override
	public void delete(T entity) {
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}

}
