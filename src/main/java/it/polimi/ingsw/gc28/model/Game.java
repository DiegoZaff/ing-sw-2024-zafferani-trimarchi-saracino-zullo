package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.actions.ActionManager;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.*;
import it.polimi.ingsw.gc28.model.errors.ErrorManager;
import it.polimi.ingsw.gc28.network.rmi.VirtualView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO : store virtualViews

public class Game {

    private ArrayList<VirtualView> listeners;

    private final ErrorManager errorManager;

    private ActionManager actionManager;
    private ArrayList<CardObjective> globalObjectives;
    private ArrayList<CardResource> faceUpResourceCards;
    private ArrayList<CardGold> faceUpGoldCards;

    private final Deck deck;

    private ArrayList<Player> players;

    private Integer firstPlayerIndex;

    public Integer getFirstPlayerIndex(){
        return firstPlayerIndex;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }


    private int nPlayers;

    private boolean hasGameStarted = false;

    public boolean getHasGameStarted(){
        return this.hasGameStarted;
    }

    public Optional<Integer> getRoundsLeft(){
        return actionManager.getRoundsLeft();
    }

    public ArrayList<CardResource> getFaceUpResourceCards() {
        return faceUpResourceCards;
    }

    public ArrayList<CardGold> getFaceUpGoldCards() {
        return faceUpGoldCards;
    }

    public ArrayList<CardObjective> getGlobalObjectives() {
        return globalObjectives;
    }

    public Game() throws IOException, IllegalArgumentException, IllegalStateException {

        this.deck = new Deck();

        this.deck.shuffleAll();

        this.players = new ArrayList<>();

        this.errorManager = new ErrorManager(this.players);

        this.listeners = new ArrayList<>();

    }

    /**
     * This method sets the number of players and initialise the action manager.
     * @param NumPlayers is the number of players.
     */
    public void setNPlayers(int NumPlayers){
        this.nPlayers = NumPlayers;
        this.actionManager = new ActionManager(nPlayers,this.players, this.errorManager, firstPlayerIndex);
    }

    /**
     * This constructor is used only for testing purposes, because a know deck
     * is passed as a parameter.
     */
    public Game(int nPlayers, Deck deck, int firstPlayerIndex) throws IllegalStateException{
        this.firstPlayerIndex = firstPlayerIndex;

        this.nPlayers = nPlayers;

        this.deck = deck;

        this.players = new ArrayList<>();

        this.errorManager = new ErrorManager(this.players);
        this.actionManager = new ActionManager(nPlayers, this.players, this.errorManager, firstPlayerIndex);
    }


    public void addPlayerToGame(String name, VirtualView client) throws RuntimeException{
        if(players.size() >= nPlayers){
            try {
                client.reportError("Game lobby is full!");
                throw new RuntimeException("Couldn't add player to the game: lobby is full");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        if(players.stream().map(Player::getName).anyMatch(pName -> pName.equals(name))){
            try {
                client.reportError("Choose another name!");
                throw new RuntimeException("Couldn't add player to the game: name already taken");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        players.add(new Player(name, client));

        actionManager.nextMove();

        checkStartGame();
    }

    private void checkStartGame(){
        if(players.size() == nPlayers){
            gameStart();
        }
    }

    private void gameStart() throws IllegalStateException {

        // initialize global objectives
        this.initGlobalObjectives();

        // initialize personal objectives
        for(Player player : players){
            Optional<CardObjective>  obj1 = deck.nextObjective();
            Optional<CardObjective> obj2 = deck.nextObjective();

            if(obj1.isEmpty() || obj2.isEmpty()){
                throw new IllegalStateException();
            }

            ArrayList<CardObjective> objectives = new ArrayList<>();
            objectives.add(obj1.get());
            objectives.add(obj2.get());

            player.setObjectivesToChoose(objectives);
        }

        //initialize visible cards
        this.initFaceUpGoldCards();
        this.initFaceUpResourceCards();

        // initialize players hands
        for(Player player : players) {
            Optional<CardResource> cResource1 = deck.nextResource();
            Optional<CardResource> cResource2 = deck.nextResource();
            Optional<CardGold> cGold = deck.nextGold();

            if(cResource1.isEmpty() || cResource2.isEmpty() || cGold.isEmpty()){
                throw new IllegalStateException();
            }
            CardResource cardResource1 = cResource1.get();
            CardResource cardResource2 = cResource2.get();
            CardGold cardGold = cGold.get();

            player.addCardToHand(cardResource1);
            player.addCardToHand(cardResource2);
            player.addCardToHand(cardGold);
        }

        for(Player player : players){
            Optional<CardInitial> cInitial1 = deck.nextInitial();

            if(cInitial1.isEmpty()){
                throw new IllegalStateException();
            }
            player.setCardInitial(cInitial1.get());
        }

        actionManager.initFirstPlayer();

        hasGameStarted = true;
    }

    /**
     * This method initializes the global objectives.
     */
    private void initGlobalObjectives() throws IllegalStateException{
        Optional<CardObjective> globObj1 = this.deck.nextObjective();
        Optional<CardObjective> globObj2 = this.deck.nextObjective();

        if(globObj1.isEmpty() || globObj2.isEmpty()){
            throw new IllegalStateException();
        }

        this.globalObjectives = new ArrayList<>();
        this.globalObjectives.add(globObj1.get());
        this.globalObjectives.add(globObj2.get());
    }

    /**
     * This method initialized the two face-up resource cards.
     */
    private void initFaceUpResourceCards() throws IllegalStateException{
        Optional<CardResource> faceUpResource1 = this.deck.nextResource();
        Optional<CardResource> faceUpResource2 = this.deck.nextResource();

        if(faceUpResource1.isEmpty() || faceUpResource2.isEmpty()){
            throw new IllegalStateException();
        }

        this.faceUpResourceCards = new ArrayList<>();
        this.faceUpResourceCards.add(faceUpResource1.get());
        this.faceUpResourceCards.add(faceUpResource2.get());
    }

    /**
     * This method initialized the two face-up gold cards.
     */
    private void initFaceUpGoldCards() throws IllegalStateException{
        Optional<CardGold> faceUpGold1 = this.deck.nextGold();
        Optional<CardGold> faceUpGold2 = this.deck.nextGold();

        if(faceUpGold1.isEmpty() || faceUpGold2.isEmpty()){
            throw new IllegalStateException();
        }

        this.faceUpGoldCards = new ArrayList<>();
        this.faceUpGoldCards.add(faceUpGold1.get());
        this.faceUpGoldCards.add(faceUpGold2.get());
    }


    /**
     * This method is responsible for checking if the player who has just
     * placed a card has reached 20 points and in that case it sets up a counter
     * which counts the rounds left.
     */
    private void checkEndGame() {

        Optional<Player> playerOfTurn = actionManager.getPlayerOfTurn();

        if(playerOfTurn.isEmpty()){
            // still in the phase of choosing objectives
            return;
        }

        boolean has20points = playerOfTurn.get().getPoints() >= 20;

        if(has20points){
            actionManager.initRoundsLeft();
        }

        else if (this.deck.nextResource().isEmpty() && this.deck.nextGold().isEmpty()){ //implementazione fine partita con 0 carte in entrambi deck

            actionManager.initRoundsLeft();

        }



    }

    /**
     * ! TODO : make it work also when deck is finished and no player has reached 20 points. IMPLEMENTED! LOOK ABOVE
     **/
    private void endGame(){
        calculateObjectivePoints();
        calculateWinner();
    }


    /**
     * play a card in a player table
     * @param playerName who is playing the card
     * @param playedCard the card to be played
     * @param isFront indicate how the card has to be played, front if True, back if False
     * @param coordinates indicates the coordinate where the card has to be played
     */
    public void playGameCard (String playerName, CardGame playedCard, boolean isFront, Coordinate coordinates ) {

        Optional<Player> player = getPlayerOfName(playerName);

        if(player.isEmpty()){
            System.err.println("Requested action for non-existent player");
            return;
        }

        ActionType actionRequested = playedCard.getIntendedAction();

        if(!actionManager.validatesMove(player.get(), actionRequested)){
            player.get().notifyError();
            return;
        }

        try{
            player.get().playCard(playedCard, isFront, coordinates);

            notifyOfCardPlayed(player.get(), playedCard, coordinates);

            // clean up all errors after a move is done successfully.
            errorManager.cleanUpAllErrors();

            // set up attributes for next turn.
            setupNextMove();

            notifyOfNextTurn();
        }catch (Exception e) {
            System.err.println("Error playing Card " + playedCard);
        }
    }

    /**
     * This method is responsible for calling '.calculatePoints(objs)' on every player when the game
     * is finished. This is a callback of 'endGame()'
     */
    private void calculateObjectivePoints(){
        for(Player player : players){
            player.calculateObjectivePoints(globalObjectives.stream()
                    .map((CardObjective::getObjective))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    /**
     * This method calculates who's the winner at the end of the game.
     * ? maybe add a winner attribute Optional<Player> and set winner player.
     */
    private void calculateWinner(){


        int maxPoints = 0;
        ArrayList<Player> winners = new ArrayList<>();

        // TODO: does this work if all players have 0 points? YES
        for (Player player : players ) {
            if (player.getPoints() > maxPoints) {
                maxPoints = player.getPoints();
                winners.clear();
                winners.add(player);
            } else if (player.getPoints() == maxPoints) {
                winners.add(player);
            }
        }

        ArrayList<Player> winnersAfterObjectivePointsCheck = new ArrayList<>();
        if (winners.size() > 1)
        {
            int maxObjectivePoints = 0;
            for (Player player : winners)
            {
                if (player.getObjectivePoints() > maxObjectivePoints)
                {
                    maxObjectivePoints = player.getObjectivePoints();
                    winnersAfterObjectivePointsCheck.clear();
                    winnersAfterObjectivePointsCheck.add(player);
                }
                else if (player.getObjectivePoints() == maxObjectivePoints)
                {
                    winnersAfterObjectivePointsCheck.add(player);
                }
            }
        }else{
            winnersAfterObjectivePointsCheck.add(winners.getFirst());
        }

        for (Player player : winnersAfterObjectivePointsCheck)
        {
            player.setWinner();
        }

        //posso farlo con functional

    }


    /**
     * This method sets everything up for the next turn by calling checkEndGame() if
     * roundsLeft has not been set, or endGame() if roundsLeft is zero.
     * It also calls actionManager.nextMove() which updates the next turn's expected action and
     * playerOfTurn.
     */
    private void setupNextMove(){
        Optional<Integer> roundsLeft = getRoundsLeft();

        if(roundsLeft.isEmpty()) {
            checkEndGame();
            actionManager.nextMove();
        }else{
            actionManager.nextMove();

            boolean hasEnded = actionManager.getActionType() == ActionType.GAME_ENDED;

            if(hasEnded){
                endGame();
            }
        }

    }

    /**
     * This method take the card from the top of the deck and add that card to the player's hand
     * @param playerName name of playing player
     */
    public void drawGameCard(String playerName, boolean fromGoldDeck){

        Optional<Player> playingPlayer = getPlayerOfName(playerName);



        if(playingPlayer.isEmpty()){
            System.err.println("Requested action for non-existent player");
            return;
        }

        ActionType actionRequested = ActionType.DRAW_CARD;
        Optional<CardResource> cardResourceOptional;
        CardResource cardResource;
        Optional<CardGold> cardGoldOptional;
        CardGold cardGold;

        if(!actionManager.validatesMove(playingPlayer.get(), actionRequested)){
            playingPlayer.get().notifyError();
            return;
        }


        if(fromGoldDeck) {
            cardGoldOptional = deck.nextGold();
            if(cardGoldOptional.isPresent()){
                cardGold = cardGoldOptional.get();
                playingPlayer.get().addCardToHand(cardGold);

                notifyOfCardDrawn(playerName, cardGold, fromGoldDeck);
            }
        }
        else {
            cardResourceOptional = deck.nextResource();
            if(cardResourceOptional.isPresent()){
                cardResource = cardResourceOptional.get();
                playingPlayer.get().addCardToHand(cardResource);

                notifyOfCardDrawn(playerName, cardResource, fromGoldDeck);
            }
        }

        actionManager.nextMove();

        notifyOfNextTurn();
    }

    public void drawGameCard(String playerName, CardResource cardDrawn){

        Optional<Player> playingPlayer = getPlayerOfName(playerName);

        if(playingPlayer.isEmpty()){
            System.err.println("Requested action for non-existent player");
            return;
        }

        ActionType actionRequested = ActionType.DRAW_CARD;

        if(!actionManager.validatesMove(playingPlayer.get(), actionRequested)){
            playingPlayer.get().notifyError();
            return;
        }

        try{
            cardDrawn.drawFaceUpCard(this.faceUpResourceCards, this.faceUpGoldCards, this.deck, playingPlayer.get());

            notifyOfCardDrawn(playerName, cardDrawn);
        }catch (RuntimeException e){
            errorManager.fromInvalidDrawMove(playingPlayer.get());
            playingPlayer.get().notifyError();
            return;
        }

        actionManager.nextMove();

        notifyOfNextTurn();
    }

    /**
     * This method is called when the user selects a personal objective card.
     */
    public void chooseObjective(String playerName, CardObjective card){
        Optional<Player> playingPlayer = getPlayerOfName(playerName);

        if(playingPlayer.isEmpty()){
            System.err.println("Requested action for non-existent player");
            return;
        }

        ActionType actionRequested = ActionType.CHOOSE_OBJ;

        if(!actionManager.validatesMove(playingPlayer.get(), actionRequested)){
            playingPlayer.get().notifyError();
            return;
        }

        Optional<ArrayList<CardObjective>> options = playingPlayer.get().getObjectivesToChoose();

        //check that player has that card
        if(options.isPresent() && options.get().contains(card)){
            playingPlayer.get().setObjectiveChosen(card);

            notifyObjChosen(playerName, card);
        }else{
            errorManager.fromInvalidObjectiveChoice(playingPlayer.get());
            playingPlayer.get().notifyError();
            return;
        }

        actionManager.nextMove();

        notifyOfNextTurn();
    }

    /**
     * this method is used to inform clients whose turn is it.
     */
    public Optional<Player>  playerToPlay(){
        return actionManager.getPlayerOfTurn();
    }

    /**
     * this method is used to inform clients the expected action form the player of turn.
     */
    public ActionType actionExpected(){
        return actionManager.getActionType();
    }


    /**
     * Returns array of winners. If array is empty, then the game is still on going.
     * If there's more than one winner, then the game ended in a draw.
     */
    public ArrayList<Player> getWinners(){
        return players.stream().filter(Player::isWinner).collect(Collectors.toCollection(ArrayList::new));
    }

    public Player getFirstPlayer(){
        return actionManager.getFirstPlayer();
    }

    public Optional<Player> getPlayerOfName(String name){
            return getPlayers().stream().filter((player -> player.getName().equals(name))).findFirst();
    }


    public void notifyOfNextTurn(){

        String nextPlayer = null;

        Optional<Player> playerOfTurn =  actionManager.getPlayerOfTurn();

        if(playerOfTurn.isPresent()){
            nextPlayer = playerOfTurn.get().getName();
        }

        for(Player player : players){
            try {
                player.getListener().onNextExpectedPlayerAction(actionManager.getActionType(), nextPlayer);
            } catch (RemoteException e) {
                System.err.println("Could not notify client " + player.getListener().toString() + " about next turn");
            }
        }
    }


    public void notifyOfCardDrawn(String playerName, CardResource card, boolean fromGoldDeck){
        for(Player player : players){
            try {
                player.getListener().onPlayerDrawnCard(playerName, card.getId(), fromGoldDeck);
            } catch (RemoteException e) {
                System.err.println("Could not notify client " + player.getListener().toString() + " about card drawn from deck");
            }
        }
    }

    public void notifyOfCardDrawn(String playerName, CardResource card){
        for(Player player : players){
            try {
                player.getListener().onPlayerDrawnCard(playerName, card.getId());
            } catch (RemoteException e) {
                System.err.println("Could not notify client " + player.getListener().toString() + " about card drawn from visible cards");
            }
        }
    }

    public void notifyOfCardPlayed(Player playerWhoPlayed, CardGame card, Coordinate coord){
        for(Player player : players){
            try {
                player.getListener().onPlayerPlayedCard(playerWhoPlayed.getName(), playerWhoPlayed.getTable(), playerWhoPlayed.getPoints());
            } catch (RemoteException e) {
                System.err.println("Could not notify client " + player.getListener().toString() + " about card card played");
            }
        }
    }

    public void notifyObjChosen(String playerName, CardObjective cardObjective){
        for(Player player : players){
            try {
                player.getListener().onPlayerChoseObjective(playerName, cardObjective.getId());
            } catch (RemoteException e) {
                System.err.println("Could not notify client " + player.getListener().toString() + " about card card played");
            }
        }
    }
}
