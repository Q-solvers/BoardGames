/*
 * $Id: IBaseService.java 22 2009-02-14 20:29:08Z iskakoff $
 */
package org.a2union.gamesystem.model.base;

/**
 * Base service object
 * Service layer is need for interact with UI-layer such as web-interface
 * or some other. Most methods of Service-layer classes delegate work to DAO(persistent)-layer.
 *
 * @author Iskakoff
 */
public interface IBaseService<T extends Base> {

    /**
     * Obtain object from persistent layer by specified id
     * @param id object identifier
     * @return object with given id or null
     */
    T getById(String id);

    /**
     * Save object
     * @param obj object to save
     * @return saved object id
     */
    String save(T obj);

    /**
     * Update object
     * @param obj object to update
     */
    void update(T obj);

    /**
     * Delete object
     * @param obj object to delete
     */
    void delete(T obj);
}
