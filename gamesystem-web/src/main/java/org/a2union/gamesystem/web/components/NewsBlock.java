/*
 * $Id: NewsBlock.java 193 2010-04-03 05:53:35Z iskakoff $
 */
package org.a2union.gamesystem.web.components;

import org.a2union.gamesystem.model.news.Article;
import org.a2union.gamesystem.model.news.INewsService;
import org.a2union.gamesystem.model.user.User;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.EventConstants;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Iskakoff
 */
public class NewsBlock {
    @Inject
    @Service("newsService")
    private INewsService newsService;

    private String title;
    private String newtext;

    private Article article;

    public List<Article> getArticles() {
        return newsService.listFreshNews();
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getText() {
        return article !=null ? article.getTitle() : "";
    }

    public String getDate() {
        return article !=null ? new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(article.getDate()) : "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewtext() {
        return newtext;
    }

    public void setNewtext(String newtext) {
        this.newtext = newtext;
    }

    @OnEvent(value = EventConstants.SUCCESS)
    @Transactional(propagation = Propagation.REQUIRED)
    public void success() {
        Article obj = new Article();
        obj.setDate(new Timestamp(System.currentTimeMillis()));
        obj.setText(newtext);
        obj.setTitle(title);
        newsService.save(obj);
    }
}
