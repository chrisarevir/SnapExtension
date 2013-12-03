package com.arevir.extension.snapchat;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RemoteViews;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SnapNotificationService extends AccessibilityService{
    ArrayList<String> notifText;

    /**
     * Handles the screen on event and notifies the manager to do some work and change the notification
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        notifText = extractor((Notification) event.getParcelableData());
        String names = "";
        int count = 0;

        String[] test = notifText.get(0).split(" ");
        if(test[0].matches("^[0-9]*$")){
            count = Integer.parseInt(test[0]);
            System.out.println("Multiple Notifications: " + test[0]);
            for(int i = 1; i < notifText.size(); i++){
                names += notifText.get(i);
            }
            System.out.println(names);
        }
        else{
            count = 1;
            test = notifText.get(1).split(" ");
            for(int i = 3; i < test.length; i++){
                names += test[i] + " ";
            }
            System.out.println("Just the one from: " + names);
        }
        // [Snapchat, New Snap from Ashley Raynera Medina!]
        // [3 new Snaps!, Brittany Peacock, anapa_chris]
        System.out.println(notifText);

        MessageManager manager = MessageManager.getInstance();
        if (manager != null) {
            if (manager.getReceiver()!=null){
                manager.notifyListener(names);
            }
        }

    }

    /**
     * We don't actually care for this method, but must be "implemented" to properly extend AccessibilityService
     */
    @Override
    public void onInterrupt() {
    }

    /**
    * Idea borrowed from DashNotifier source code, uses the output differently :D
    * Copyright (c) 2013 Umang Vipul under The MIT License (MIT)
    * */
    public static ArrayList<String> extractor(Notification notification) {
        ArrayList<String> notifText = new ArrayList<String>();
        RemoteViews views = notification.contentView;
        @SuppressWarnings("rawtypes")
        Class secretClass = views.getClass();

        try {

            Field outerFields[] = secretClass.getDeclaredFields();
            for (int i = 0; i < outerFields.length; i++) {

                if (!outerFields[i].getName().equals("mActions"))
                    continue;

                outerFields[i].setAccessible(true);

                @SuppressWarnings("unchecked")
                ArrayList<Object> actions = (ArrayList<Object>) outerFields[i].get(views);
                for (Object action : actions) {

                    Field innerFields[] = action.getClass().getDeclaredFields();

                    Object value = null;
                    Integer type = null;
                    @SuppressWarnings("unused")
                    Integer viewId = null;
                    for (Field field : innerFields) {

                        field.setAccessible(true);
                        if (field.getName().equals("value")) {
                            value = field.get(action);
                        } else if (field.getName().equals("type")) {
                            type = field.getInt(action);
                        } else if (field.getName().equals("viewId")) {
                            viewId = field.getInt(action);
                        }
                    }

                    if (type != null && (type == 9 || type == 10) && value != null) {
                        if (!notifText.contains(value.toString()))
                            notifText.add(value.toString());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifText;
    }
}