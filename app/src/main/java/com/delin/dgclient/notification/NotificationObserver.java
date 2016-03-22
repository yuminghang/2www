package com.delin.dgclient.notification;

/**
 * Created by cdm on 15/5/29.
 */
public class NotificationObserver {

    private String id;
    private String[] notificationNames;
    private NotificationListener notificationListener;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getNotificationNames() {
        return notificationNames;
    }

    public void setNotificationNames(String[] notificationNames) {
        this.notificationNames = notificationNames;
    }

    public NotificationListener getNotificationListener() {
        return notificationListener;
    }

    public void setNotificationListener(NotificationListener notificationListener) {
        this.notificationListener = notificationListener;
    }

}
