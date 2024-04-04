package it.polimi.ingsw.gc28.model;

import java.io.FileReader;

import it.polimi.ingsw.gc28.games.Move;
import it.polimi.ingsw.gc28.model.actions.utils.ActionType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Deck deck;
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

            JSONArray moves = (JSONArray) jsonObject.get("moves");

            this.moveList = new ArrayList<>();

            for (Object move : moves) {
                JSONObject moveObj = (JSONObject) move;
                String player = (String) moveObj.get("player");
                String actionStr = (String) moveObj.get("action");
                ActionType action = ActionType.valueOf(actionStr);
                boolean isFront = (boolean) moveObj.get("isFront");

                Move moveObject = new Move(player, action, isFront);
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
            if(move.getAction() == ActionType.PLAY_INITIAL_CARD){
                // do something
            }else  if(move.getAction() == ActionType.PLAY_CARD){
                // do something
            }else if (move.getAction() == ActionType.CHOOSE_OBJ){
                // do something
            }else if(move.getAction() == ActionType.DRAW_CARD){
                // do something
            }else if(move.getAction() == ActionType.GAME_ENDED){
                // do something
            }


        }
    }
}
