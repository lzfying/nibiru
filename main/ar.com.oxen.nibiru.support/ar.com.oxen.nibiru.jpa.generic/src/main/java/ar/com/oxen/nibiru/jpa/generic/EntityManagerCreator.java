package ar.com.oxen.nibiru.jpa.generic;

import javax.persistence.EntityManager;

interface EntityManagerCreator {
	EntityManager create();
}