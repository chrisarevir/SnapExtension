package com.arevir.extension.snapchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessageReceiver extends BroadcastReceiver {
    @SuppressWarnings("unused")
    private boolean userActive = false;
    private boolean onScreen = false;

    private MessageManager mManager;

    /**
     * Constructor for the message handler
     */
    public MessageReceiver(MessageManager manager){
        mManager=manager;
    }

    /**
     * Handles the intent, setting the variables accordingly
     * @param context passed as default, nothing done to it
     * @param intent used to evaluate what to set the variables
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            System.out.println("SCREEN ON");
            onScreen = true;
            userActive=false;
        }else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            System.out.println("SCREEN OFF");
            onScreen=false;
            userActive=false;
        }else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            userActive=true;
            System.out.println("PAST LOCKSCREEN");
            if (onScreen && mManager!=null){
                mManager.clearCount();
            }
        }
    }
}
