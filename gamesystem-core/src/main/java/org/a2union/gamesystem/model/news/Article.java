/*
 * $Id: Article.java 193 2010-04-03 05:53:35Z iskakoff $
 */
package org.a2union.gamesystem.model.news;

import org.a2union.gamesystem.model.base.Base;
import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author Iskakoff
 */
@Entity
@Table(name = "tbl_article")
public class Article extends Base {

    /**
     * Date of article publishing
     */
    @Column(name = "DATE_COLUMN")
    private Timestamp date;
    /**
     * store article text
     */
    @Column(name = "TEXT_COLUMN")
    @Lob
    @Basic(fetch = LAZY)
    private String text;

    @Column(name = "TITLE_COLUMN", length = 50)
    private String title;

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
