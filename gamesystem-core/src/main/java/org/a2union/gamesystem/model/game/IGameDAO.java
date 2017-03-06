/*
 * $Id: $
 */
package org.a2union.gamesystem.model.game;

import org.a2union.gamesystem.model.base.IBaseDAO;
import org.a2union.gamesystem.model.game.step.Step;

import java.util.List;
import java.util.Map;

/**
 * @author Iskakoff
 */
public interface IGameDAO<T extends GameBase> extends IBaseDAO<T> {

    int countItems(Map<String, Object> parameters, String query);

    List<T> getItems(Map<String, Object> parameters, String query);

    List<T> getItemsPage(Map<String, Object> parameters, String query, int startIndex, int size);

    Step getLastStepByGame(GameBase game);

    List<Step> getAllSteps(T game);
}
