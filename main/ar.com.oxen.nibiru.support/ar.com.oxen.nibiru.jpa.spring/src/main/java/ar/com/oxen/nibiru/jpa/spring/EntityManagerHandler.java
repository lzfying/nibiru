package ar.com.oxen.nibiru.jpa.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.persistence.EntityManager;

class EntityManagerHandler implements InvocationHandler {
	private EntityManager proxied;

	static EntityManager buidlProxy(EntityManager entityManager) {
		return (EntityManager) Proxy.newProxyInstance(entityManager.getClass()
				.getClassLoader(), new Class<?>[] { EntityManager.class },
				new EntityManagerHandler(entityManager));
	}

	private EntityManagerHandler(EntityManager proxied) {
		super();
		this.proxied = proxied;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (method.getName().equals("close")) {
			return null;
		} else {
			return method.invoke(this.proxied, args);
		}
	}

}