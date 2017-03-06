/*
 * $Id: IBaseDAO.java 113 2009-09-12 19:28:19Z iskakoff $
 */
package org.a2union.gamesystem.model.base;

import java.util.List;

/**
 * @author Iskakoff
 */
public interface IBaseDAO<T extends Base> {
    /**
     * Return the persistent object instance with the given identifier,
	 * or null if there is no such persistent instance.
     *
     * @param id object identifier
     * @return persistent object instance or null
     */
    T getById(String id);

    /**
     * Save persistent object instance
     * 
     * @param obj object to persist
     * @return object identifier
     */
    String save(T obj);

    /**
     * Update persistent object instance state
     *
     * @param obj object to update
     */
    void update(T obj);

    /**
     * Drop object instance
     * @param obj object to delete
     */
    void delete(T obj);

    /**
     *
     * @return all object of intarface class objects
     */
    List<T> getAll();

    int countObjects();
}
