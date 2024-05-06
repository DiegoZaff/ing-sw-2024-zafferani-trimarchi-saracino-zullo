package it.polimi.ingsw.gc28.model.chat;

public class Chat {

    public void addMessage(){
        //qui va preso il messaggio, convertito in string e aggiunto alla lista di messaggi
        // già inviati precedentemente, dopo aver fatto questo, in controller può fare la notify
        //e quindi restituire al client un aggiornamento della game representation che contiene anche la
        //chat che quindi viene aggiornata con il nuovo messaggio inviato
    }
}
