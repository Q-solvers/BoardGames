/*
 * $Id: INewsService.java 34 2009-03-08 17:47:36Z iskakoff $
 */
package org.a2union.gamesystem.model.news;

import org.a2union.gamesystem.model.base.IBaseService;

import java.util.List;

/**
 * @author Iskakoff
 */
public interface INewsService extends IBaseService<Article> {
    List<Article> listFreshNews();
}
