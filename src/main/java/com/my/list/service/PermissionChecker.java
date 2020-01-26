package com.my.list.service;

import com.my.list.domain.MainData;
import com.my.list.domain.User;

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
    public void check(MainData mainData, boolean readOnly) {
        if (mainData == null) throw new DataException("Input mainData is null.");
        String permission = mainData.getPermission();
        if (permission == null) throw new AuthException("Permission is null");
        boolean success;
        switch (permission) {
            case "public" :
                success = true;
                break;
            case "protect" :
                success = readOnly || userId.equals(mainData.getUser());
                break;
            case "private" :
                success = userId.equals(mainData.getUser());
                break;
            default:
                throw new AuthException("Unknown permission: " + permission);
        }
        if (!success) throw new AuthException("Permission denied, permission=" + mainData.getPermission() +
            ", expectedUserId=" + mainData.getUser() + ", actualUserId=" + userId);
    }
}
