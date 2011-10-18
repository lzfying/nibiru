package ar.com.oxen.nibiru.dynamicbundle.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import ar.com.oxen.nibiru.dynamicbundle.dao.api.DynamicBundleDao;
import ar.com.oxen.nibiru.dynamicbundle.domain.DynamicBundle;

public class JpaDynamicBundleDao implements DynamicBundleDao {
	@PersistenceContext(type=PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<DynamicBundle> findAll() {
		return (List<DynamicBundle>) this.entityManager.createQuery(
				"select o from DynamicBundle o").getResultList();
	}

	@Override
	public void update(DynamicBundle entity) {
		this.entityManager.persist(entity);

	}
}
