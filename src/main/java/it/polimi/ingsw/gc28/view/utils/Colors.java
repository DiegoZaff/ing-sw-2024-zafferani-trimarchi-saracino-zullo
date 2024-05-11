package it.polimi.ingsw.gc28.view.utils;

public enum Colors {
    RED ("\u001B[31m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    WHITE ("\u001B[37m"),
    RESET("\u001B[0m");

    private String code;

    public String getCode(){
        return code;
    }

    private Colors(String code){
        this.code = code;
    }

}
