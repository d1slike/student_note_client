package ru.disdev;

import ru.disdev.model.UserInfo;

/**
 * Created by DisDev on 22.07.2016.
 */
public class UserSettings {

    private static volatile UserInfo userInfo;

    public static String getCurrentUserId() {
        return userInfo.getId();
    }

    public static synchronized void setCurrentUser(UserInfo info) {
        UserSettings.userInfo = info;
    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static boolean needShowElderMenu() {
        return userInfo.isCanEditThisGroup() || userInfo.isCanMakeNewPost();
    }

    public static boolean hasGroup() {
        return userInfo.getGroupId() != -1;
    }


}
