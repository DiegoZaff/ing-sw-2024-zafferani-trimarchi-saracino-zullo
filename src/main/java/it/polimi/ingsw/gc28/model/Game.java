package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.utils.PlayerColor;
import it.polimi.ingsw.gc28.view.GameRepresentation;
import it.polimi.ingsw.gc28.view.PrivateRepresentation;
import it.polimi.ingsw.gc28.model.actions.ActionManager;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.*;
import it.polimi.ingsw.gc28.model.chat.Chat;
import it.polimi.ingsw.gc28.model.chat.ChatMessage;
import it.polimi.ingsw.gc28.model.errors.PlayerActionError;
import it.polimi.ingsw.gc28.model.errors.types.*;


import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Game implements Serializable {

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

    private String gameId;


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

    private Chat chat;

    public Game(int nPlayers, String gameId) throws IOException, IllegalArgumentException, IllegalStateException {

        this.nPlayers = nPlayers;

        this.deck = new Deck();

        this.deck.shuffleAll();

        this.players = new ArrayList<>();

        this.actionManager = new ActionManager(nPlayers, this.players);

        if(gameId != null){
            this.gameId = gameId;
        }else{
            this.gameId = generateRandomGameId();
        }
        this.chat = new Chat();
    }

    /**
     * This constructor is used only for testing purposes, because a know deck
     * is passed as a parameter.
     */
    public Game(int nPlayers, Deck deck, int firstPlayerIndex, String gameId) throws IllegalStateException{
        if(gameId != null){
            this.gameId = gameId;
        }else{
            this.gameId = generateRandomGameId();
        }

        this.firstPlayerIndex = firstPlayerIndex;

        this.nPlayers = nPlayers;

        this.deck = deck;

        this.players = new ArrayList<>();

        this.actionManager = new ActionManager(nPlayers, this.players, firstPlayerIndex);
    }


    public void addPlayerToGame(String name) throws PlayerActionError {
        if(players.size() >= nPlayers){
            throw new LobbyFullError();
        }

        if(players.stream().map(Player::getName).anyMatch(pName -> pName.equals(name))){
            throw new NameAlreadyChosenError();
        }

        players.add(new Player(name));

        actionManager.nextMove();

        checkStartGame();
    }

    public int getActualNumPlayers(){
        return players.size();
    }

    public int getNPlayers(){
        return nPlayers;
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
        actionManager.initPlayerOfTurn();  //aggiunta inizializzazione

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
     * This method initializes the two face-up resource cards.
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
     * This method initializes the two face-up gold cards.
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

        else if (this.deck.isResEmpty() && this.deck.isGoldEmpty()){ //implementazione fine partita con 0 carte in entrambi deck

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
    public void playGameCard (String playerName, CardGame playedCard, boolean isFront, Coordinate coordinates ) throws  PlayerActionError{

        Optional<Player> player = getPlayerOfName(playerName);

        if(player.isEmpty()){
            throw new NoSuchPlayerError();
        }

        ActionType actionRequested = playedCard.getIntendedAction();

        // this throws errors if move is not valid
        actionManager.validatesMove(player.get(), actionRequested);

        // this can throw errors.
        try {
            player.get().playCard(playedCard, isFront, coordinates);
        } catch (PlayerActionError e){
            throw new PlayerActionError (e.getError());
        }
        // set up attributes for next turn.
        setupNextMove();
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
    public CardResource drawGameCard(String playerName, boolean fromGoldDeck) throws PlayerActionError{

        Optional<Player> playingPlayer = getPlayerOfName(playerName);

        if(playingPlayer.isEmpty()){
            throw new NoSuchPlayerError();
        }

        ActionType actionRequested = ActionType.DRAW_CARD;
        Optional<CardResource> cardResourceOptional;
        CardResource cardResource;
        Optional<CardGold> cardGoldOptional;
        CardGold cardGold;

        actionManager.validatesMove(playingPlayer.get(), actionRequested);

        if(fromGoldDeck) {
            cardGoldOptional = deck.nextGold();
            if(cardGoldOptional.isPresent()){
                cardGold = cardGoldOptional.get();
                playingPlayer.get().addCardToHand(cardGold);

                actionManager.nextMove();

                return cardGold;
            }

            throw new DeckHaveNoMoreCards("gold");
        }
        else {
            cardResourceOptional = deck.nextResource();
            if(cardResourceOptional.isPresent()){
                cardResource = cardResourceOptional.get();
                playingPlayer.get().addCardToHand(cardResource);

                actionManager.nextMove();

                return cardResource;
            }

            throw new DeckHaveNoMoreCards("gold");
        }

    }

    public void drawGameCard(String playerName, CardResource cardDrawn) throws PlayerActionError{

        Optional<Player> playingPlayer = getPlayerOfName(playerName);

        if(playingPlayer.isEmpty()){
            throw new NoSuchPlayerError();
        }

        ActionType actionRequested = ActionType.DRAW_CARD;

        actionManager.validatesMove(playingPlayer.get(), actionRequested);

        cardDrawn.drawFaceUpCard(this.faceUpResourceCards, this.faceUpGoldCards, this.deck, playingPlayer.get());

        actionManager.nextMove();

    }

    /**
     * This method is called when the user selects a personal objective card.
     */
    public void chooseObjective(String playerName, CardObjective card) throws PlayerActionError{
        Optional<Player> playingPlayer = getPlayerOfName(playerName);

        if(playingPlayer.isEmpty()){
            throw new NoSuchPlayerError();
        }

        ActionType actionRequested = ActionType.CHOOSE_OBJ;

        actionManager.validatesMove(playingPlayer.get(), actionRequested);

        Optional<ArrayList<CardObjective>> options = playingPlayer.get().getObjectivesToChoose();

        //check that player has that card
        if(options.isPresent() && options.get().contains(card)){
            playingPlayer.get().setObjectiveChosen(card);

        }else{
            throw new InvalidObjectiveChoiceError();
        }

        actionManager.nextMove();
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

    /**
     * This method generates a random id associated to a new game.
     * @return game id converted to a string.
     */
    public String generateRandomGameId(){
        // TODO : use same method of other!! uuid
        Random random = new Random();
        int id = random.nextInt(1000000000);
        return Integer.toString(id);
    }

    /**
     * @return gameId
     */
    public String getGameId(){
        return gameId;
    }

    private ArrayList<String> getPlayersNickname(){
        ArrayList<String> nicknames = new ArrayList<>();
        for (Player p : players){
            nicknames.add(p.getName());
        }
        return nicknames;
    }


    private Map<String, Integer> getPointsMap(){
        Map<String, Integer> points = new HashMap<>();
        for (Player p : players){
            points.put(p.getName(), p.getPoints());
        }

        return points;
    }

    public Map<String, PrivateRepresentation> getPrivateRepresentationsMap(){
        Map<String, PrivateRepresentation> privateRepresentations = new HashMap<>();

        for (Player p : players){
            privateRepresentations.put(p.getName(), p.getState());
        }

        return privateRepresentations;
    }

    private ArrayList<String> getFaceUpGoldCardsIDs(){
        ArrayList<String> faceUpCards = new ArrayList<>();

        for (Card c : faceUpGoldCards){
            faceUpCards.add(c.getId());
        }

        return faceUpCards;
    }
    private ArrayList<String> getFaceUpResourceCardsIDs(){
        ArrayList<String> faceUpCards = new ArrayList<>();

        for (Card c : faceUpResourceCards){
            faceUpCards.add(c.getId());
        }

        return faceUpCards;
    }
    private ArrayList<String> getObjectiveIDs(){
        ArrayList<String> faceUpCards = new ArrayList<>();

        for (Card c : globalObjectives){
            faceUpCards.add(c.getId());
        }

        return faceUpCards;
    }


    public GameRepresentation getGameRepresentation(){

        Optional<Player> playerOfTurn = actionManager.getPlayerOfTurn();

        String playerToPlayName;

        playerToPlayName = playerOfTurn.map(Player::getName).orElse(null);

        ActionType actionExpected = actionExpected();

        return new GameRepresentation(playerToPlayName, actionExpected ,this.getPlayersNickname(),
                this.getObjectiveIDs(), this.getFaceUpResourceCardsIDs() ,this.getFaceUpGoldCardsIDs(),
                deck.getNextResourceCard().getId(), deck.getNextGoldCard().getId(),
                this.getPointsMap(), this.getPrivateRepresentationsMap(), this.getChat());
    }

    public void sendMessage(ChatMessage chatMessage){
        chat.addMessage(chatMessage);
    }

    public Chat getChat(){
        return chat;
    }

    public void chooseColor(String playerName, String color) throws NoSuchPlayerError, ColorTakenError, InvalidColor {
        Optional<Player> player;
        player = getPlayerOfName(playerName);

        if(player.isEmpty()){
            throw new NoSuchPlayerError();
        }

        PlayerColor col;
        try{
            col = PlayerColor.customValueOf(color);
        } catch (IllegalArgumentException e){
            throw new InvalidColor(color);
        }

        boolean isColorTaken = this.players.stream().anyMatch(p -> col.equals(p.getColor()));
        if(isColorTaken){
            throw new ColorTakenError();
        }
        player.get().setColor(col);
        actionManager.nextMove();
    }

    public void setWaitForReconnections() throws UnrestorableGameError {
        actionManager.setWaitForReconnections();
    }



    public void reconnectPlayer(String name) throws NoSuchPlayerError, PlayerIsAlreadyConnectedError {
        Optional<Player> player = getPlayerOfName(name);

        if(player.isEmpty()){
            throw new NoSuchPlayerError();
        }

        if(player.get().isConnected()){
            throw new PlayerIsAlreadyConnectedError(name);
        }

        player.get().setConnected(true);

        actionManager.nextMove();
    }


    public int getNPlayersToReconnect(){
        return (int) players.stream().filter(p -> !p.isConnected()).count();
    }

    public int getPlayersToJoin(){
        return getNPlayers() - getActualNumPlayers();
    }
    public boolean isEveryoneReconnected(){
        return players.stream().allMatch(Player::isConnected);
    }

}
