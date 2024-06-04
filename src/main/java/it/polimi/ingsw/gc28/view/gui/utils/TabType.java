package it.polimi.ingsw.gc28.view.gui.utils;

public enum TabType {
    GAMES,
    MENU,
    LOBBY,
    IN_GAME;

    @Override
    public String toString() {
        switch (this){
            case GAMES -> {
                return "games";
            }
            case MENU -> {
                return "menu";
            }
            case LOBBY -> {
                return "lobby";
            }
            case IN_GAME -> {
                return "inGame";
            }
        }
        return super.toString();
    }

    public TabType getPreviousTab(){
        switch (this){
            case GAMES, MENU -> {
                return TabType.MENU;
            }
            case LOBBY, IN_GAME -> {
                return TabType.GAMES;
            }
        }
        return TabType.MENU;
    }

    public String getTabTitle(){
        switch (this){
            case GAMES -> {
                return "Games";
            }
            case MENU -> {
                return "Menu";
            }
            case LOBBY -> {
                return "Lobby";
            }
            case IN_GAME -> {
                return "In Game";
            }
        }
        return super.toString();
    }
}
