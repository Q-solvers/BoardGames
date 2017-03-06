/*
 * $Id: ShowError.java 90 2009-06-30 17:54:28Z iskakoff $
 */
package org.a2union.gamesystem.web.components;


import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.corelib.internal.InternalMessages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

/**
 * This component will be used to display form errors or will be deleted if standard t:errors will be used
 *
 * @author Iskakoff S.
 */
public class ShowError {
    @Inject
    @Path(value = "context:images/error-icon.png")
    private Asset error;

    @Environmental(false)
    private ValidationTracker tracker;

    public Asset getError() {
        return error;
    }

    public void setError(Asset error) {
        this.error = error;
    }

    @BeforeRenderTemplate
    public void beforeRender(){
        if(tracker == null)
            throw new RuntimeException(InternalMessages.encloseErrorsInForm());
    }

    public boolean getInError(){
        return tracker.getHasErrors();
    }

    public String getFirstError(){
        List<String> errors = tracker.getErrors();
        if(errors==null || errors.isEmpty())
            return "";
        return errors.iterator().next();
    }
}
