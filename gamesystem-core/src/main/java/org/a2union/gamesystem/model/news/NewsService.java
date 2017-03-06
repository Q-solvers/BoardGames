/*
 * $Id: NewsService.java 90 2009-06-30 17:54:28Z iskakoff $
 */
package org.a2union.gamesystem.model.news;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Iskakoff
 */
public class NewsService implements INewsService {
    private INewsDAO newsDAO;

    public Article getById(String id) {
        return newsDAO.getById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String save(Article obj) {
        return newsDAO.save(obj);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Article obj) {
        newsDAO.update(obj);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Article obj) {
        newsDAO.delete(obj);
    }

    public void setNewsDAO(INewsDAO newsDAO) {
        this.newsDAO = newsDAO;
    }

    public List<Article> listFreshNews() {
        return newsDAO.listFreshNews();
    }
}
