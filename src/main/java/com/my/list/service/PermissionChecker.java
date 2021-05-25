package com.my.list.service;

import com.my.list.entity.MainData;
import com.my.list.entity.User;
import com.my.list.exception.DataException;
import com.my.list.exception.ForbiddenException;

import java.util.Objects;

public class PermissionChecker {
    
    private final Long userId;

    public PermissionChecker(User user) {
        if (user == null) throw new DataException("User is null");
        if (user.getId() == null) throw new DataException("Id of user is null");
        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
    public void check(MainData mainData, boolean write) {
        if (mainData == null) throw new DataException("Input mainData is null.");
        String permission = mainData.getPermission();
        if (permission == null) throw new ForbiddenException("Permission is null");
        boolean success;
        switch (permission) {
            case "public" :
                success = true;
                break;
            case "protect" :
                success = !write || userId.equals(mainData.getUser());
                break;
            case "private" :
                success = userId.equals(mainData.getUser());
                break;
            default:
                throw new ForbiddenException("Unknown permission: " + permission);
        }
        if (!success) throw new ForbiddenException("Permission denied, permission=" + mainData.getPermission() +
            ", expectedUserId=" + userId + ", actualUserId=" + mainData.getUser());
    }
    public void checkUpdate(MainData original, MainData updated) {
        this.check(original, true);
        if (!Objects.equals(userId, original.getUser())) {

            if (!Objects.equals(updated.getUser(), original.getUser()))
                throw new DataException("Can't change owner for shared node, nodeId=" + original.getId() + ", from="
                    + original.getPermission() + ", to=" + updated.getPermission() + ".");
            
            if (!Objects.equals(updated.getPermission(), original.getPermission()))
                throw new DataException("Can't change permission for shared node, nodeId=" + original.getId() + ", from="
                    + original.getPermission() + ", to=" + updated.getPermission() + ".");
        }
    }
}
