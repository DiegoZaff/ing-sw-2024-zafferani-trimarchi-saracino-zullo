package it.polimi.ingsw.gc28.model;

import java.io.FileReader;

import it.polimi.ingsw.gc28.games.assertions.*;
import it.polimi.ingsw.gc28.games.moves.Move;
import it.polimi.ingsw.gc28.games.TestingDeck;
import it.polimi.ingsw.gc28.games.assertions.utils.GameAssertionType;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.*;
import it.polimi.ingsw.gc28.model.errors.types.PlayerActionError;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;
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




            this.deckCopy = new TestingDeck(deckCardResourcesArray,
                    deckCardGoldArray,
                    deckCardInitialArray,
                    deckCardObjectivesArray);

            ArrayList<CardResource> cardResourcesOrdered = this.deckCopy.cardResourcesOrdered;
            ArrayList<CardGold> cardGoldOrdered = this.deckCopy.cardGoldOrdered;
            ArrayList<CardInitial> cardInitialOrdered = this.deckCopy.cardInitialOrdered;
            ArrayList<CardObjective> cardObjectiveOrdered = this.deckCopy.cardObjectiveOrdered;



            this.deck = new Deck(cardResourcesOrdered,
                    cardGoldOrdered,
                    cardInitialOrdered,
                    cardObjectiveOrdered);

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
                            String card = (String) astObj.get("card");
                            String namePlayer = (String) astObj.get("name");
                            boolean isPlayedFront = (boolean) astObj.get("isFront");
                            int x = ((Long) astObj.get("x")).intValue();
                            int y = ((Long) astObj.get("y")).intValue();

                            gameAssertion = new CardPlayedAtGameAssertion(card, namePlayer, x, y, isPlayedFront);
                            gameAssertions.add(gameAssertion);

                        }else if(type.equals(GameAssertionType.PLAYERS_REGISTERED)){
                            int value = ((Long) astObj.get("value")).intValue();

                            gameAssertion = new PlayerRegisteredGameAssertion(value);
                            gameAssertions.add(gameAssertion);

                        }else if(type.equals(GameAssertionType.GAME_STARTED)){
                            boolean state = (boolean) astObj.get("state");

                            gameAssertion = new GameStartedGameAssertion(state);
                            gameAssertions.add(gameAssertion);

                        }else if(type.equals(GameAssertionType.FIRST_PLAYER)){
                            String namePlayer = (String) astObj.get("value");

                            gameAssertion = new FirstPlayerGameAssertion(namePlayer);
                            gameAssertions.add(gameAssertion);

                        }else if(type.equals(GameAssertionType.GLOBAL_OBJ)){
                            String card1 = (String) astObj.get("card1");
                            String card2 = (String) astObj.get("card2");

                            gameAssertion = new GlobalObjectivesGameAssertion(card1, card2);
                            gameAssertions.add(gameAssertion);

                        }else if(type.equals(GameAssertionType.FACE_UP_CARDS_RES)){
                            String card1 = (String) astObj.get("card1");
                            String card2 = (String) astObj.get("card2");

                            gameAssertion = new FaceUpCardsResGameAssertion(card1, card2);
                            gameAssertions.add(gameAssertion);
                        }else if(type.equals(GameAssertionType.FACE_UP_CARDS_GOLD)){
                            String card1 = (String) astObj.get("card1");
                            String card2 = (String) astObj.get("card2");

                            gameAssertion = new FaceUpCardsGoldGameAssertion(card1, card2);
                            gameAssertions.add(gameAssertion);
                        }else if(type.equals(GameAssertionType.OBJS_TO_CHOOSE)){
                            String card1 = (String) astObj.get("card1");
                            String card2 = (String) astObj.get("card2");
                            String namePlayer = (String) astObj.get("name");

                            gameAssertion = new ObjectiveToChooseAssertion(card1, card2, namePlayer);
                            gameAssertions.add(gameAssertion);

                        }else if(type.equals(GameAssertionType.OBJ_CHOSEN)){
                            String card = (String) astObj.get("card");
                            String namePlayer = (String) astObj.get("name");

                            gameAssertion = new ObjChosenAssertion(namePlayer, card);
                            gameAssertions.add(gameAssertion);

                        } else if(type.equals(GameAssertionType.HAND_OF_PLAYER)) {
                            String card1 = (String) astObj.get("card1");
                            String card2 = (String) astObj.get("card2");
                            String card3 = (String) astObj.get("card3");
                            String namePlayer = (String) astObj.get("name");

                            gameAssertion = new HandOfPlayerAssertion(card1,card2,card3, namePlayer);
                            gameAssertions.add(gameAssertion);
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

                        } else if(type.equals(GameAssertionType.ROUNDS_LEFT)){
                            int value = ((Long) astObj.get("value")).intValue();

                            gameAssertion = new RoundsLeftAssertion(value);
                            gameAssertions.add(gameAssertion);
                        } else if(type.equals(GameAssertionType.POINTS_OF_PLAYER_FROM_OBJS)){
                            String nick = (String) astObj.get("nick");
                            int value = ((Long) astObj.get("value")).intValue();

                            gameAssertion = new PointsPlayerFromObjsAssertion(nick, value);
                            gameAssertions.add(gameAssertion);
                        } else if(type.equals(GameAssertionType.RESOURCES_VISIBLE)){
                            String playerName = (String) astObj.get("name");
                            ResourcePrimaryType resType = ResourcePrimaryType.valueOf((String) astObj.get("resType"));
                            int value = ((Long) astObj.get("number")).intValue();

                            gameAssertion = new ResourcePrimaryVisibleGameAssertion(resType, playerName, value);
                            gameAssertions.add(gameAssertion);

                        }else if(type.equals(GameAssertionType.NEXT_EXPECTED_ACTION)){
                            ActionType actionEx = ActionType.valueOf((String) astObj.get("value"));

                            gameAssertion = new NextExpectedActionAssertion(actionEx);
                            gameAssertions.add(gameAssertion);
                        }
                    }
                }

                Move moveObject =  Move.createMove(player, action, isFront, cardR, cardG, cardI,cardO, coord, fromGoldDeck, gameAssertions);
                moveList.add(moveObject);
            }

            Object firstPlayerObj = jsonObject.get("firstPlayer");
            int firstPlayer = ((Long) firstPlayerObj).intValue();

            this.game = new Game(2, this.deck, firstPlayer);

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
            try {
                move.play(game);
            } catch (PlayerActionError e) {
                // TODO: add assertions for exceptions!
                System.out.println(e.getMessage());
            }

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
