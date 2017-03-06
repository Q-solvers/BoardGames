/*
 * $Id: Base.java 90 2009-06-30 17:54:28Z iskakoff $
 */
package org.a2union.gamesystem.model.base;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Base persistent class
 *
 * @author Iskakoff
 */
@MappedSuperclass
public abstract class Base {
    // object identifier (database primary key)
    @Id()
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "org.hibernate.id.UUIDHexGenerator")    
    @Column(name = "UUID")
    private String UUID;
    // object description for using in subclasses
    // TODO think about usages of remarks
    @Transient
    //@Column(name = "REMARK_COLUMN", length = 4000)
    private String remark;
    @Transient
    private Base parent;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Base getParent() {
        return parent;
    }

    public void setParent(Base parent) {
        this.parent = parent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
