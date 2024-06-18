package it.polimi.ingsw.gc28.model.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chat implements Serializable {
    private ArrayList<ChatMessage> chat;
    private final int maxVisibleMessages = 40;

    /**
     * Constructor.
     */
    public Chat(){
        this.chat = new ArrayList<>();
    }

    public void addMessage(ChatMessage message){

        if(chat.size() > maxVisibleMessages){
            chat.removeFirst();
        }
        chat.add(message);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        int c = 0;
        int maxLen = 0;
        for (ChatMessage message : chat) {
            int messageLength = message.getText().length();
            if (messageLength > maxLen) {
                maxLen = messageLength;
            }
        }
        for (ChatMessage message : chat) {
            if(!message.isPrivate()) {
                builder.append(message.toString(c, maxLen, false));
                builder.append("\n");
                c++;
            }
        }
        String result = null;
        if (builder.toString().endsWith("\n")) {
            result = builder.substring(0, builder.toString().length() - 1);
        }
        return result;
    }

    public String toString(String viewer, String player) {
        StringBuilder builder = new StringBuilder();
        int c = 0;
        int maxLen = 0;
        for (ChatMessage message : chat) {
            int messageLength = message.getText().length();
            if (messageLength > maxLen) {
                maxLen = messageLength;
            }
        }
        for (ChatMessage message : chat) {
            boolean isBetweenViewerAndPlayer =
                    (message.getSender().equals(viewer) && message.getReceiver().equals(player)) ||
                            (message.getSender().equals(player) && message.getReceiver().equals(viewer));

            if (isBetweenViewerAndPlayer) {
                builder.append(message.toString(c, maxLen, true));
                builder.append("\n");
                c++;
            }
        }
        String result = null;
        if (builder.toString().endsWith("\n")) {
            result = builder.substring(0, builder.toString().length() - 1);
        }
        return result;
    }



    public ArrayList<ChatMessage> getChat(){
        return chat;
    }
}

//qui va preso il messaggio, convertito in string e aggiunto alla lista di messaggi
// già inviati precedentemente, dopo aver fatto questo, in controller può fare la notify
//e quindi restituire al client un aggiornamento della game representation che contiene anche la
//chat che quindi viene aggiornata con il nuovo messaggio inviato

//in to string posso passare un parametro del receiver, se chiede global chat, printo filtrando all, altrimenti filtro per playername.
