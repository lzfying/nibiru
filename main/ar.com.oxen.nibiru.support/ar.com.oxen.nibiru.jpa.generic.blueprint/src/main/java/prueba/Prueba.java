package prueba;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

// TODO: probablemente se vaya este modulo completo
public class Prueba {
	public EntityManagerFactory getObject() {
//		ClassLoader cl =  Thread.currentThread().getContextClassLoader();
//		Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("nibiruPersistenceUnit");
//		Thread.currentThread().setContextClassLoader(cl);
		
		return emf;
	}
}
