/*
 * $Id: INewsDAO.java 47 2009-03-25 07:47:13Z iskakoff $
 */
package org.a2union.gamesystem.model.news;

import org.a2union.gamesystem.model.base.IBaseDAO;

import java.util.List;

/**
 * @author Iskakoff
 */
public interface INewsDAO extends IBaseDAO<Article> {
    /**
     * Get actual news
     * @return list of FRESH_NEWS_COUNT actual news
     */
    List<Article> listFreshNews();
}
