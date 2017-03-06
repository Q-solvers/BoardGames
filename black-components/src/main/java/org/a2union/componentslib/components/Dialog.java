/*
 * $Id: Dialog.java 147 2009-10-17 01:24:09Z iskakoff $
 */
package org.a2union.componentslib.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

/**
 * @author Iskakoff
 */
@IncludeStylesheet(value = "classpath:org/a2union/componentslib/Dialog.css")
public class Dialog {

    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Parameter(required = false, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String zone;

    @Parameter
    private Object[] context;
    @Persist(value = PersistenceConstants.FLASH)
    private Object[] cntx;
    @Persist(value = PersistenceConstants.FLASH)
    private boolean renderBody;
    @Component
    private Form dialogForm;
    @Inject
    private Block formBlock;

    @Component(parameters = {"event=cancel"})
    private EventLink cancel;

    @Inject
    private ComponentResources resources;
    @Inject
    private Request request;

    public void setContext(Object... cntx) {
        this.context = cntx;
    }

    public Object[] getContext() {
        return context;
    }

    public Object[] getCntx() {
        return cntx;
    }

    public void setCntx(Object... cntx) {
        this.cntx = cntx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZone() {
        return zone;
    }

    public Block getFormBlock() {
        return formBlock;
    }

    public Object onActionFromDialogAction(Object... context) {
        setCntx(context);
        renderBody = (true);
        return resources.getPage();
    }

    public Object onActionFromDialogZoneAction(Object... context) {
        setCntx(context);
        return getFormBlock();
    }

    public Object onCancel() {
        return resources.getPage();
    }


    public boolean isLink() {
        return isValid() && !renderBody;
    }

    //Delegating to form

    public boolean isValid() {
        return dialogForm.isValid();
    }

    public boolean getHasError() {
        return dialogForm.getHasErrors();
    }

    public void recordError(String message) {
        dialogForm.recordError(message);
    }

    public void recordError(Field field, String message) {
        dialogForm.recordError(field, message);
    }
}