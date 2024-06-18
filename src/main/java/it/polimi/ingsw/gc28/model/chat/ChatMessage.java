package it.polimi.ingsw.gc28.model.chat;

import java.io.Serializable;
import java.time.LocalTime;

public class ChatMessage implements Serializable {
    private String text;
    private String sender;
    private String receiver;
    private LocalTime time;
    private boolean isPrivate;

    /**
     * Constructor.
     * @param text is the body of the message.
     * @param sender is the player who sent the message.
     */
    public ChatMessage(String text, String sender, String receiver, boolean isPrivate){
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.time = java.time.LocalTime.now();
        this.isPrivate = isPrivate;
    }
    public String getText(){
        return text;
    }

    public String toString(int i, int len, boolean isPrivate){
        String priv = "[Private] ";
        if (!isPrivate){
            priv = "";
        }

        String time = String.format("[%02d:%02d:%02d] ", this.time.getHour(), this.time.getMinute(), this.time.getSecond());
        String messageText = priv + time + sender + ": " + this.text;
        int paddingLength = Math.max(0, len - messageText.length());
        String padding = " ".repeat(paddingLength);
        return messageText + padding;
    }
    public boolean isPrivate() {
        return isPrivate;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getTime(){
        return String.format("[%02d:%02d:%02d] ", this.time.getHour(), this.time.getMinute(), this.time.getSecond());
    }
}

