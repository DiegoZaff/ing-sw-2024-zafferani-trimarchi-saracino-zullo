package it.polimi.ingsw.gc28.model;

import it.polimi.ingsw.gc28.model.utils.GameEndedNotification;
import it.polimi.ingsw.gc28.model.utils.JoinInfo;
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

/**
 * this is the most important class of the project, it contains the methods
 * that make playing the game possible, every game ha shis own game class and there
 * are a minimum of 2 to maximum of 4 players per game.
 * In this class there are methods for every phase of the game from start to finish.
 */
public class Game implements Serializable {

    private ActionManager actionManager;
    /**
     * is a list of the public objectives for the game, there are 2 CardObjectives for each game
     */
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

    /**
     * it's an alphanumerical string randomly generated and unique for every game
     */
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

    /**
     * this method adds the player in the parameter in the game only if there are still available spots in the game.
     * it also checks whether the nickname is already taken in the game.
     * @param name
     * @throws PlayerActionError
     */
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

    /**
     * this method is used to check when the lobby is full
     */
    private void checkStartGame(){
        if(players.size() == nPlayers){
            gameStart();
        }
    }

    /**
     * this method starts the game calling all the initialization methods for players, deck, and the
     * flow of the game.
     * @throws IllegalStateException
     */
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
        }else if (this.deck.isResEmpty() && this.deck.isGoldEmpty()){ //implementazione fine partita con 0 carte in entrambi deck
            actionManager.initRoundsLeftDecksFinished();
        }



    }

    /**
     * this method starts the final part of the game when the objective points are calculated and the winner is announced
     * @throws GameEndedNotification
     */
    private void endGame() throws GameEndedNotification {
        calculateObjectivePoints();
        calculateWinner();
    }


    /**
     * this method plays a card in a player table, checks wheather the action requested is valid and
     * sets up the next move to be played
     * @param playerName who is playing the card
     * @param playedCard the card to be played
     * @param isFront indicate how the card has to be played, front if True, back if False
     * @param coordinates indicates the coordinate where the card has to be played
     */
    public void playGameCard (String playerName, CardGame playedCard, boolean isFront, Coordinate coordinates ) throws PlayerActionError, GameEndedNotification {

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
    private void calculateWinner() throws GameEndedNotification {
        int maxPoints = 0;
        ArrayList<Player> winners = new ArrayList<>();


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

        throw new GameEndedNotification();
    }


    /**
     * This method sets everything up for the next turn by calling checkEndGame() if
     * roundsLeft has not been set, or endGame() if roundsLeft is zero.
     * It also calls actionManager.nextMove() which updates the next turn's expected action and
     * playerOfTurn.
     */
    private void setupNextMove() throws GameEndedNotification {
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
     * @param fromGoldDeck is a boolean that tells from which deck the card needs to be drawn
     * @return the drawn card
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

                try {
                    setupNextMove();
                } catch (GameEndedNotification ignored) {
                    // this cant happen
                }

                return cardGold;
            }

            throw new DeckHaveNoMoreCards("gold");
        }
        else {
            cardResourceOptional = deck.nextResource();
            if(cardResourceOptional.isPresent()){
                cardResource = cardResourceOptional.get();
                playingPlayer.get().addCardToHand(cardResource);

                try {
                    setupNextMove();
                } catch (GameEndedNotification ignored) {
                    // this cant happen
                }

                return cardResource;
            }

            throw new DeckHaveNoMoreCards("resource");
        }

    }

    /**
     * this method draws a face up card form the table
     * @param playerName that draws the card
     * @param cardDrawn is the chosen card from the player to draw
     * @throws PlayerActionError
     */
    public void drawGameCard(String playerName, CardResource cardDrawn) throws PlayerActionError{

        Optional<Player> playingPlayer = getPlayerOfName(playerName);

        if(playingPlayer.isEmpty()){
            throw new NoSuchPlayerError();
        }

        ActionType actionRequested = ActionType.DRAW_CARD;

        actionManager.validatesMove(playingPlayer.get(), actionRequested);

        cardDrawn.drawFaceUpCard(this.faceUpResourceCards, this.faceUpGoldCards, this.deck, playingPlayer.get());

        try {
            setupNextMove();
        } catch (GameEndedNotification ignored) {
            // happens only after playing a card
        }

    }

    /**
     * This method is called when the user selects a personal objective card.
     *
     * @param playerName that has chosen the objective
     * @param card objective chosen
     * @throws PlayerActionError
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

    /**
     * Returns the player randomly selected as the firstPlayer
     */
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

        CardResource nextResourceCard = deck.getNextResourceCard();

        CardGold nextGoldCard = deck.getNextGoldCard();

        return new GameRepresentation(playerToPlayName, actionExpected ,this.getPlayersNickname(),
                this.getObjectiveIDs(), this.getFaceUpResourceCardsIDs() ,this.getFaceUpGoldCardsIDs(),
                nextResourceCard != null ? nextResourceCard.getId() : null, nextGoldCard != null ? nextGoldCard.getId() : null,
                this.getPointsMap(), this.getPrivateRepresentationsMap(), this.getChat(), this.getNPlayers(),
                this.getRoundsLeft().isPresent() ? this.getRoundsLeft().get() : null);
    }

    public void sendMessage(ChatMessage chatMessage){
        chat.addMessage(chatMessage);
    }

    public Chat getChat(){
        return chat;
    }

    /**
     * this method chooses the color specified to the player passed as a parameter
     * @param playerName that has chosen the color
     * @param color chosen from the player
     * @throws PlayerActionError
     */
    public void chooseColor(String playerName, String color) throws PlayerActionError {
        Optional<Player> player;
        player = getPlayerOfName(playerName);

        if(player.isEmpty()){
            throw new NoSuchPlayerError();
        }

        ActionType actionRequested =  ActionType.CHOOSE_COLOR;

        // this throws errors if move is not valid
        actionManager.validatesMove(player.get(), actionRequested);

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

        if(player.get().getColor() != null){
            throw new PlayerAlreadyChoseColorError(player.get().getName());
        }

        player.get().setColor(col);
        actionManager.nextMove();
    }

    public void setWaitForReconnections() throws UnrestorableGameError {
        actionManager.setWaitForReconnections();
    }


    /**
     * this method is called when a player reconnects to he game and needs to be added back
     * @param name
     * @throws NoSuchPlayerError
     * @throws PlayerIsAlreadyConnectedError
     */
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

    /**
     * this method checks if every player in the game is connected and the game can restart
     * @return a boolean
     */
    public boolean isEveryoneReconnected(){
        return players.stream().allMatch(Player::isConnected);
    }

    public Optional<JoinInfo> getJoinInfo(){
        if(!actionExpected().equals(ActionType.JOIN_GAME)){
            return Optional.empty();
        }

        ArrayList<String> playersIn = getPlayersNickname();
        JoinInfo info = new JoinInfo(getGameId(), playersIn, nPlayers);
        return Optional.of(info);
    }

}
