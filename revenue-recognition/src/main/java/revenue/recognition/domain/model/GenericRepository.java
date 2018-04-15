package revenue.recognition.domain.model;

import java.io.Serializable;

public interface GenericRepository<T, ID extends Serializable> {

	T findOne(ID id);

	T save(T entity);

	T update(T entity);
	
	void delete(ID id);

	void delete(T entity);
	
}
