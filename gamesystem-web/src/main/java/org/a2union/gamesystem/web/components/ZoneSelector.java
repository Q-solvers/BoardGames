package org.a2union.gamesystem.web.components;

import org.a2union.gamesystem.model.game.zone.GameZoneType;
import org.a2union.gamesystem.web.services.SessionService;
import org.a2union.gamesystem.web.pages.Index;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @author Iskakoff
 */
public class ZoneSelector {

    private GameZoneType type;

    @Inject
    private SessionService service;

    void setupRender(){
        setType(service.getCurrentGameZone().getType());
    }

    @Validate("required")
    public GameZoneType getType() {
        return type;
    }

    public void setType(GameZoneType type) {
        this.type = type;
    }

    public Object onSuccess() {
        if (type != null)
            service.setCurrentGameZone(type);
        switch (type) {
            case REVERSI_ZONE:
                return Index.class;
            default:
                return Index.class;
        }
    }

}
