package org.a2union.gamesystem.commons;

import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.User;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Iskakoff
 */
public class UsersDataSource implements GridDataSource {
    private IUserService userService;
    private String username;
    private Iterator<User> result;

    private static Map<String, String> sortColumnMapping = new HashMap<String, String>();

    static {
        sortColumnMapping.put("UUID", "login.username");
        sortColumnMapping = Collections.unmodifiableMap(sortColumnMapping);
    }

    @Override
    public int getAvailableRows() {
        return userService.usersCount() - 1;
    }

    @Override
    public Object getRowValue(int index) {
        if (result.hasNext())
            return result.next();
        return null;
    }

    @Override
    public Class getRowType() {
        return User.class;
    }

    @Override
    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
        StringBuffer orderBy = new StringBuffer();
        for (SortConstraint sortConstraint : sortConstraints) {
            String str = sortColumnMapping.get(sortConstraint.getPropertyModel().getPropertyName());
            if(!ColumnSort.UNSORTED.equals(sortConstraint.getColumnSort()) && StringUtils.isNotBlank(str)) {
                orderBy.append(" order by ");
                orderBy.append(str);
                orderBy.append(" ");
                orderBy.append(ColumnSort.ASCENDING.equals(sortConstraint.getColumnSort()) ? "ASC" : "DESC");
            }
        }
        result = userService.getOtherUsersPagedOrdered(username, orderBy.toString(), startIndex, endIndex - startIndex + 1).iterator();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
}
