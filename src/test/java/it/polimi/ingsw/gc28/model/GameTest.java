package it.polimi.ingsw.gc28.model;

import java.io.FileReader;

import it.polimi.ingsw.gc28.games.assertions.*;
import it.polimi.ingsw.gc28.games.moves.Move;
import it.polimi.ingsw.gc28.games.TestingDeck;
import it.polimi.ingsw.gc28.games.assertions.utils.GameAssertionType;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Deck deck;

    private TestingDeck deckCopy;
    private Game game;

    private ArrayList<Move> moveList;

    private static ArrayList<Integer> convertJSONArrayToListInteger(JSONArray jsonArray) {
        ArrayList<Integer> list = new ArrayList<>();
        for (Object obj : jsonArray) {
            list.add(((Long) obj).intValue());
        }
        return list;
    }

    private static ArrayList<String> convertJSONArrayToListStrings(JSONArray jsonArray) {
        ArrayList<String> list = new ArrayList<>();
        for (Object obj : jsonArray) {
            list.add((String) obj);
        }
        return list;
    }

    private void readGame(int i) throws IOException {
        JSONParser jsonParser = new JSONParser();
        String path = "./src/test/java/it/polimi/ingsw/gc28/games/Game" + i + ".json";
        FileReader reader = new FileReader(path);

        try {
            Object obj = jsonParser.parse(reader);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray deckCardResources = (JSONArray) jsonObject.get("deckCardResources");
            ArrayList<String> deckCardResourcesArray = convertJSONArrayToListStrings(deckCardResources);

            JSONArray deckCardGold = (JSONArray) jsonObject.get("deckCardGold");
            ArrayList<String> deckCardGoldArray = convertJSONArrayToListStrings(deckCardGold);

            JSONArray deckCardInitial = (JSONArray) jsonObject.get("deckCardInitial");
            ArrayList<String> deckCardInitialArray = convertJSONArrayToListStrings(deckCardInitial);

            JSONArray deckCardObjectives = (JSONArray) jsonObject.get("deckCardObjectives");
            ArrayList<String> deckCardObjectivesArray = convertJSONArrayToListStrings(deckCardObjectives);

            JSONArray players = (JSONArray) jsonObject.get("players");
            ArrayList<String> playersList = convertJSONArrayToListStrings(players);


            this.deck = new Deck(deckCardResourcesArray,
                    deckCardGoldArray,
                    deckCardInitialArray,
                    deckCardObjectivesArray);

            this.deckCopy = new TestingDeck(deckCardResourcesArray,
                    deckCardGoldArray,
                    deckCardInitialArray,
                    deckCardObjectivesArray);

            JSONArray moves = (JSONArray) jsonObject.get("moves");

            this.moveList = new ArrayList<>();

            for (Object move : moves) {
                JSONObject moveObj = (JSONObject) move;
                String player = (String) moveObj.get("player");
                String actionStr = (String) moveObj.get("action");
                ActionType action = ActionType.valueOf(actionStr);


                Optional<CardObjective> cardObj = null;
                Optional<CardInitial> cardInitial = null;
                Optional<CardResource> cardResource = null;
                Optional<CardGold> cardGold = null;
                CardObjective cardO = null;
                CardInitial cardI = null;
                CardResource cardR = null;
                CardGold cardG = null;

                Coordinate coord = null;
                Boolean isFront = null;
                Boolean fromGoldDeck = null;
                String name = null;


                if(action.equals(ActionType.PLAY_INITIAL_CARD)){
                    Object idObj = moveObj.get("cardId");
                    String cardId = (String) idObj;
                    cardInitial = deckCopy.getCardInitialFromId(cardId);
                    cardI = cardInitial.get();

                    isFront = (Boolean) moveObj.get("isFront");

                }else if(action.equals(ActionType.PLAY_CARD)){
                    Object idObj = moveObj.get("cardId");
                    String cardId = (String) idObj;
                    if(cardId.charAt(0) == 'R') {
                        cardResource = deckCopy.getCardResFromId(cardId);
                        cardR = cardResource.get();
                    } else {
                        cardGold = deckCopy.getCardGoldFromId(cardId);
                        cardG = cardGold.get();
                    }

                    isFront = (Boolean) moveObj.get("isFront");

                    Object xObj = moveObj.get("x");
                    int x = ((Long) xObj).intValue();
                    Object yObj = moveObj.get("y");
                    int y = ((Long) yObj).intValue();
                    coord = new Coordinate(x,y);

                }else if(action.equals(ActionType.CHOOSE_OBJ)){
                    Object idObj = moveObj.get("cardId");
                    String cardId = (String) idObj;
                    cardObj = deckCopy.getCardObjectiveFromId(cardId);
                    cardO = cardObj.get();

                }else if(action.equals(ActionType.DRAW_CARD)){
                    Object idObj = moveObj.get("cardId");
                    if(idObj != null){
                        String cardId = (String) idObj;
                        if(cardId.charAt(0) == 'R') {
                            cardResource = deckCopy.getCardResFromId(cardId);
                            cardR = cardResource.get();
                        } else {
                            cardGold = deckCopy.getCardGoldFromId(cardId);
                            cardG = cardGold.get();
                        }
                    }else{
                        fromGoldDeck = (boolean) moveObj.get("fromGoldDeck");
                    }
                }

                ArrayList<GameAssertion> gameAssertions = new ArrayList<>();
                // read assertions
                JSONArray assertions = (JSONArray) moveObj.get("assertions");
                if(assertions != null){
                    for(Object ast : assertions){
                        JSONObject astObj = (JSONObject) ast;

                        String astType = (String) astObj.get("type");
                        GameAssertionType type = GameAssertionType.valueOf(astType);

                        GameAssertion gameAssertion;

                        if(type.equals(GameAssertionType.CARD_PLAYED_AT)){
                            String nick = (String) astObj.get("nick");
                            String cardId = (String) astObj.get("card");

                            gameAssertion = new CardPlayedAtGameAssertion();
                        }else if(type.equals(GameAssertionType.PLAYERS_REGISTERED)){

                        }else if(type.equals(GameAssertionType.GAME_STARTED)){

                        }else if(type.equals(GameAssertionType.FIRST_PLAYER)){

                        }else if(type.equals(GameAssertionType.GLOBAL_OBJ)){

                        }else if(type.equals(GameAssertionType.FACE_UP_CARDS_RES)){

                        }else if(type.equals(GameAssertionType.FACE_UP_CARDS_GOLD)){

                        }else if(type.equals(GameAssertionType.OBJS_TO_CHOOSE)){

                        }else if(type.equals(GameAssertionType.OBJ_CHOSEN)){

                        } else if(type.equals(GameAssertionType.HAND_OF_PLAYER)) {

                        } else if(type.equals(GameAssertionType.CARD_PLAYED_AT)){

                        }
                        else if(type.equals(GameAssertionType.POINTS_OF_PLAYER)){
                            String nick = (String) astObj.get("nick");
                            Object pointsObj = astObj.get("value");
                            int points = ((Long) pointsObj).intValue();

                            gameAssertion = new PointsPlayerGameAssertion(points, nick);
                            gameAssertions.add(gameAssertion);

                        }else if(type.equals(GameAssertionType.NEXT_TURN_PLAYER)){
                            String nick = (String) astObj.get("value");

                            gameAssertion = new NextPlayerTurnGameAssertion(nick);
                            gameAssertions.add(gameAssertion);

                        }else if(type.equals(GameAssertionType.IS_WINNER)){
                            JSONArray nicksArray = (JSONArray) astObj.get("nicks");
                            ArrayList<String> nicks = convertJSONArrayToListStrings(nicksArray);

                            gameAssertion = new WinnerGameAssertion(nicks);
                            gameAssertions.add(gameAssertion);

                        }
                    }
                }

                Move moveObject =  Move.createMove(player, action, isFront, cardR, cardG, cardI,cardO, coord, fromGoldDeck, gameAssertions);
                moveList.add(moveObject);
            }

            Object firstPlayerObj = jsonObject.get("firstPlayer");
            int firstPlayer = ((Long) firstPlayerObj).intValue();

            this.game = new Game(4, this.deck, firstPlayer);

        } catch (Exception e) {
            fail("Error while reading Game " + i + ":\n" + e.getMessage());
        }

    }

    /**
     * Parameterized test for game simulation. WIP
     * @param i index of game
     */
    @ParameterizedTest
    @ValueSource(ints = {1})
    public void game(int i){
        try {
            this.readGame(i);
            assertTrue(true);
        } catch (IOException e) {
            fail("fail to read game " + i);
        }

        for(Move move: moveList){
            move.play(game);

            // verify assertions after the move
            for (GameAssertion gameAssertion : move.getAssertions()){
                if(gameAssertion.verifyAssertion(game)){
                   assertTrue(true, "Game assertion: " + gameAssertion);
                }else{
                    fail("Game assertion: " + gameAssertion);
                }
            }
        }
    }
}
