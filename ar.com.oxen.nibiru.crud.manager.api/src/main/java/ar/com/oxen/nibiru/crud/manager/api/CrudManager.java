package ar.com.oxen.nibiru.crud.manager.api;

import java.util.List;

/**
 * Service for managing CRUD over entities.
 * 
 * @param <T>
 *            The crud entity type.
 */
public interface CrudManager<T> {
	/**
	 * Returns the entity type name.
	 * 
	 * The name identifies the kind of entity being handled. This is useful, for
	 * example, in order to determine if a given entity is compatible with a
	 * crud manager.
	 * 
	 * @return The type name.
	 */
	String getEntityTypeName();

	/**
	 * Gets the fields to be shown in the entity list.
	 * 
	 * @return A list with the fields
	 */
	List<CrudField> getListFields();

	/**
	 * Gets the fields to be shown in the entity form.
	 * 
	 * @return A list with the fields
	 */
	List<CrudField> getFormFields();

	/**
	 * Reads all the entities.
	 * 
	 * @return A list with the entities
	 */
	List<CrudEntity<T>> findAll();

	/**
	 * Reads entities filtering by a given field. Useful for parent-child
	 * relations
	 * 
	 * @return A list with the entities
	 */
	List<CrudEntity<T>> findByfield(String field, Object value);

}
