package org.a2union.gamesystem.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @author Iskakoff
 */
public class CustomPageLink {
    @Inject
    private ComponentResources resources;

    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String page;

    @Parameter
    private Object[] context;

    void beginRender(MarkupWriter writer)
    {
        if (isCurrent()) return;

        Link link = resources.createPageLink(page, resources.isBound("context"), context);
        writer.element("a", "href", link.toURI());
        resources.renderInformalParameters(writer);
    }

    void afterRender(MarkupWriter writer)
    {
        if (isCurrent()) return;
        writer.end(); // </a>
    }

    private boolean isCurrent() {
        return String.CASE_INSENSITIVE_ORDER.compare(resources.getPageName(), page) == 0;
    }
}
