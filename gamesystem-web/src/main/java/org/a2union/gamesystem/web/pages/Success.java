package org.a2union.gamesystem.web.pages;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 *
 * @author Iskakoff
 */
public class Success {
    @Persist(PersistenceConstants.FLASH)
    private String msgCode;
    @Inject
    private ComponentResources resources;

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsg() {
        return resources.getMessages().get(getMsgCode());
    }

    public String getMsgFull() {
        String code = getMsgCode() + "-full";
        if(resources.getMessages().contains(code))
            return resources.getMessages().get(code);
        return "";
    }
}
