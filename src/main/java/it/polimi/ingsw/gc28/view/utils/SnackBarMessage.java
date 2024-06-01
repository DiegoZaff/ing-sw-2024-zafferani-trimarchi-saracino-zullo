package it.polimi.ingsw.gc28.view.utils;

import it.polimi.ingsw.gc28.view.utils.InformationType;

public class SnackBarMessage {
    private final String text;
    private final InformationType type;

    public SnackBarMessage(String text, InformationType type) {
        this.text = text;
        this.type = type;
    }

    public InformationType getType() {
        return type;
    }

    public String getText() {
        return text;
    }


    public String getStyleClass(){
        switch (getType()){
            case ERROR -> {
                return "hbox-style error-style";
            }
            case GAME_INFO -> {
                return "hbox-style success-style";
            }
            case CHAT_MESSAGE -> {
                return "hbox-style chat-msg-style";
            }
        }
        return "";
    }

    public String getImageName(){
        switch (getType()){
            case ERROR -> {
                return "error_outline.png";
            }
            case GAME_INFO -> {
                return "check_circle_outline.png";
            }
            case CHAT_MESSAGE -> {
                return "chat_info.png";
            }
        }
        return "";
    }
}
