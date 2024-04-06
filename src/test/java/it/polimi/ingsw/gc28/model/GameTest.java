package it.polimi.ingsw.gc28.model;

import java.io.FileReader;

import it.polimi.ingsw.gc28.games.assertions.GameAssertion;
import it.polimi.ingsw.gc28.games.Move;
import it.polimi.ingsw.gc28.games.TestingDeck;
import it.polimi.ingsw.gc28.games.assertions.NextPlayerTurnGameAssertion;
import it.polimi.ingsw.gc28.games.assertions.PointsPlayerGameAssertion;
import it.polimi.ingsw.gc28.games.assertions.WinnerGameAssertion;
import it.polimi.ingsw.gc28.games.assertions.utils.GameAssertionType;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import it.polimi.ingsw.gc28.model.cards.CardGame;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
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

            JSONArray deckCardResourcesPermutation = (JSONArray) jsonObject.get("deckCardResourcesPermutation");
            ArrayList<Integer> deckCardResourcesPermutationArray = convertJSONArrayToListInteger(deckCardResourcesPermutation);

            JSONArray deckCardGoldPermutation = (JSONArray) jsonObject.get("deckCardGoldPermutation");
            ArrayList<Integer> deckCardGoldPermutationArray = convertJSONArrayToListInteger(deckCardGoldPermutation);

            JSONArray deckCardInitialPermutation = (JSONArray) jsonObject.get("deckCardInitialPermutation");
            ArrayList<Integer> deckCardInitialPermutationArray = convertJSONArrayToListInteger(deckCardInitialPermutation);

            JSONArray deckCardObjectivesPermutation = (JSONArray) jsonObject.get("deckCardObjectivesPermutation");
            ArrayList<Integer> deckCardObjectivesPermutationArray = convertJSONArrayToListInteger(deckCardObjectivesPermutation);

            JSONArray players = (JSONArray) jsonObject.get("players");
            ArrayList<String> playersList = convertJSONArrayToListStrings(players);


            this.deck = new Deck(deckCardResourcesPermutationArray,
                    deckCardGoldPermutationArray,
                    deckCardInitialPermutationArray,
                    deckCardObjectivesPermutationArray);

            this.deckCopy = new TestingDeck(deckCardResourcesPermutationArray,
                    deckCardGoldPermutationArray,
                    deckCardInitialPermutationArray,
                    deckCardObjectivesPermutationArray);

            JSONArray moves = (JSONArray) jsonObject.get("moves");

            this.moveList = new ArrayList<>();

            for (Object move : moves) {
                JSONObject moveObj = (JSONObject) move;
                String player = (String) moveObj.get("player");
                String actionStr = (String) moveObj.get("action");
                ActionType action = ActionType.valueOf(actionStr);


                CardObjective cardObj = null;
                CardGame card = null;
                Coordinate coord = null;
                Boolean isFront = null;

                if(action.equals(ActionType.PLAY_INITIAL_CARD)){
                    Object indexObj = moveObj.get("cardIndex");
                    int index = ((Long) indexObj).intValue();

                    card = deckCopy.deckCardInitials.get(index);

                    isFront = (Boolean) moveObj.get("isFront");
                }else if(action.equals(ActionType.PLAY_CARD)){
                    Object indexObj = moveObj.get("cardIndex");
                    int index = ((Long) indexObj).intValue();

                    boolean isGold = (boolean) moveObj.get("isGold");

                    if(isGold){
                        card = deckCopy.deckCardGold.get(index);
                    }else{
                        card = deckCopy.deckCardResource.get(index);
                    }
                    Object xObj = moveObj.get("x");
                    int x = ((Long) xObj).intValue();
                    Object yObj = moveObj.get("y");
                    int y = ((Long) yObj).intValue();

                    coord = new Coordinate(x,y);

                    isFront = (Boolean) moveObj.get("isFront");

                }else if(action.equals(ActionType.CHOOSE_OBJ)){
                    Object indexObj = moveObj.get("cardIndex");
                    int index = ((Long) indexObj).intValue();

                    cardObj = deckCopy.deckCardObjective.get(index);
                }else if(action.equals(ActionType.DRAW_CARD)){
                    Object indexObj = moveObj.get("cardIndex");
                    int index = ((Long) indexObj).intValue();
                    boolean isGold = (boolean) moveObj.get("isGold");

                    if(isGold){
                        card = deckCopy.deckCardGold.get(index);
                    }else{
                        card = deckCopy.deckCardResource.get(index);
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

                        if(type.equals(GameAssertionType.POINTS_OF_PLAYER)){
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

                Move moveObject = new Move(player, action, isFront, card, cardObj, coord, gameAssertions);
                moveList.add(moveObject);
            }

            this.game = new Game(playersList, this.deck);

        } catch (Exception e) {
            fail("Error while reading Game " + i);
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
            fail("fail to read game 1");
        }

        for(Move move: moveList){
            Optional<Player> playingPlayer = game.getPlayers().stream().filter(p -> p.getName().equals(move.getPlayer())).findFirst();
            if(playingPlayer.isEmpty()){
                fail("Error initialization of Game: Non existent player");
            }
            if(move.getAction() == ActionType.PLAY_INITIAL_CARD){
                Optional<CardGame> cardToPlay = move.getCard();
                Optional<Boolean> isFront = move.isFront();
                if(cardToPlay.isEmpty() || isFront.isEmpty()){
                    fail("Error initialization of Game");
                }

                game.playGameCard(playingPlayer.get(), cardToPlay.get(), isFront.get(), new Coordinate(0,0) );

            }else  if(move.getAction() == ActionType.PLAY_CARD){
                Optional<CardGame> cardToPlay = move.getCard();
                Optional<Coordinate> coord = move.getCoord();
                Optional<Boolean> isFront = move.isFront();

                if(cardToPlay.isEmpty() || coord.isEmpty() || isFront.isEmpty()){
                    fail("Error initialization of Game");
                }
                game.playGameCard(playingPlayer.get(), cardToPlay.get(), isFront.get(), coord.get());

            }else if (move.getAction() == ActionType.CHOOSE_OBJ){
                Optional<CardObjective> cardObj = move.getCardObjective();

                if(cardObj.isEmpty()){
                    fail("Error initialization of Game");
                }

                game.chooseObjective(playingPlayer.get(), cardObj.get());
                // do something
            }else if(move.getAction() == ActionType.DRAW_CARD){
                Optional<CardGame> cardToDraw = move.getCard();

                if(cardToDraw.isEmpty()){
                    fail("Error initialization of Game");
                }

                game.drawGameCard(playingPlayer.get(), cardToDraw.get());
            }

            // verify assertions after the move
            for (GameAssertion ass : move.getAssertions()){
                if(ass.verifyAssertion(game)){
                   assertTrue(true, "Game assertion: " + ass);
                }else{
                    fail("Game assertion: " + ass);
                }
            }
        }
    }
}
