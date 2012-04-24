/**
 * 
 */
package ar.com.oxen.nibiru.jpa.spring;

import javax.persistence.EntityManager;

interface EntityManagerCreator {
	EntityManager create();
}