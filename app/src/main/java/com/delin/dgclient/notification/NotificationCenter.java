package com.delin.dgclient.notification;

import android.os.Handler;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.facade.Facade;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import java.util.UUID;

/**
 * Created by cdm on 15/5/29.
 */
public class NotificationCenter {
    private static final Facade facade;
    private static NotificationCenter instance = new NotificationCenter();
    private Handler handler = new Handler();

    static {
        facade = Facade.getInstance("main");
    }

    private NotificationCenter(){

    }

    public static NotificationCenter getInstance(){
        return instance;
    }

    public NotificationObserver register(final String[] notificationNames, final NotificationListener notificationListener){

        String id = UUID.randomUUID().toString();

        facade.registerMediator(new Mediator(id, null){
            @Override
            public void handleNotification(final INotification notification) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(notificationListener != null){
                            Notification noti = new Notification();
                            noti.setName(notification.getName());
                            noti.setBody(notification.getBody());
                            notificationListener.handleNotification(noti);
                        }
                    }
                });
            }

            @Override
            public String[] listNotificationInterests() {
                return notificationNames;
            }
        });

        NotificationObserver notificationObserver = new NotificationObserver();
        notificationObserver.setId(id);
        notificationObserver.setNotificationNames(notificationNames);
        notificationObserver.setNotificationListener(notificationListener);

        return notificationObserver;
    }

    public void unregister(String notificationObserverId){
        facade.removeMediator(notificationObserverId);
    }

    public void sendNotification(String notificationName){
        facade.sendNotification(notificationName);
    }

    public void sendNotification(String notificationName, Object body){
        facade.sendNotification(notificationName, body);
    }
}
