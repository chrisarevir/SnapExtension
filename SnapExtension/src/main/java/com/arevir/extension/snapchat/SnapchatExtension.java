package com.arevir.extension.snapchat;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

/**
 * Extension that focuses the work of all the other classes to display the notification
 */
public class SnapchatExtension extends DashClockExtension{
    private MessageManager  manager;
    private MessageReceiver receiver;
    String extraMessage;
    //private String names;
    /**
     * Standard initialization as well as initializing the receiver if needed
     */
    @Override
    protected void onInitialize(boolean isReconnect) {
        super.onInitialize(isReconnect);
        if (!isReconnect) {
            manager = MessageManager.getInstance(this);
            registerReceiver();
            manager.setReceiver(receiver);
        }
        // Checks and updates the widget every time screen turns on
        setUpdateWhenScreenOn(true);
    }

    /**
     * Updates the message (i.e. changes the notification)
     * Method became useful once expandedBody (extraMessage) was added
     */
    public void updateMessage(String extra) {
        extraMessage = extra;
        onUpdateData(UPDATE_REASON_CONTENT_CHANGED);
    }

    /**
     * Standard update of the widget, or wipes it when no notifications
     * @param reason automatically handled, but not used for any calculations here
     */
    @Override
    protected void onUpdateData(int reason) {
        if (manager !=null){
            PackageManager pm = getPackageManager();
            Intent intent=pm.getLaunchIntentForPackage("com.snapchat.android");

            int count= manager.getCount();
            if(count > 0){
                String message = count == 1 ? (count + " new snap"):(count + " new snaps");
                ExtensionData data =
                        new ExtensionData().visible(true)
                                .icon(R.drawable.ic_notification)
                                .status("" + count)
                                .expandedBody("From " + extraMessage)
                                .expandedTitle(message);
                if (intent!=null)
                    data.clickIntent(intent);
                publishUpdate(data);
            }
            else
                publishUpdate(null);
        }
    }

    /**
     * Sets the receiver and what it should listen to
     */
    private void registerReceiver(){
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        localIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        localIntentFilter.addAction(Intent.ACTION_USER_PRESENT);
        MessageManager manager = MessageManager.getInstance();
        if (manager!=null){
            receiver = new MessageReceiver(manager);
            registerReceiver(receiver, localIntentFilter);
        }
    }

    /**
     * delete() for the class and unregister-ing the receiver
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver !=null)
            unregisterReceiver(receiver);
    }
}