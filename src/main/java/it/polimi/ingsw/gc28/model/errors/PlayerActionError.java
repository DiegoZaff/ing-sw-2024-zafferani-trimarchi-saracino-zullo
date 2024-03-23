package it.polimi.ingsw.gc28.model.errors;

public abstract class PlayerActionError {
    private final String error;

    public PlayerActionError(String error) {
        this.error = error;
    }

    public String getError(){
        return error;
    }

}
