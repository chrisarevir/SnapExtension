package com.arevir.extension.snapchat;

public class MessageManager {

    private SnapchatExtension ext;
    private static MessageManager instance;
    private MessageReceiver receiver;
    private int count;
    private String message;

    /**
     * Set receiver
     * @param context The extension passed in that we will report to, and initializes our manager
     */
    private MessageManager(SnapchatExtension context) {
        ext = context;
        count = 0;
    }

    /**
     * Neat constructor for our manager
     * @return      the instance which is the manager for this service
     */
    public static MessageManager getInstance(SnapchatExtension context) {
        if (instance == null) {
            instance = new MessageManager(context);
        }
        return instance;
    }
    /**
     * Set receiver
     * @return      the instance which is the manager for this service
     */
    public static MessageManager getInstance() {
        return instance;
    }

    /**
     * Notify the extension to update itself on new snap
     */
    public void notifyListener(int notifCount, String notifMessage) {
        if (ext != null){
            count = notifCount;
            message = notifMessage;
        }
    }

    /**
     * Resets the counter
     */
    public void clearCount(){
        count = 0;
    }

    /**
     * @return      the total count of snaps not yet viewed
     */
    public int getCount() {
        return count;
    }


    /**
     * @return      the string of people who have sent you a snap
     */
    public String getMessage() { return message; }

    /**
     * @return      the receiver for this service
     */
    public MessageReceiver getReceiver() {
        return receiver;
    }

    /**
     * Set receiver
     * @param mReceiver The receiver that will get all the messages for this service
     */
    public void setReceiver(MessageReceiver mReceiver) {
        this.receiver = mReceiver;
    }
}