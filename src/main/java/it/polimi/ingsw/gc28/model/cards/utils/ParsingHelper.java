package it.polimi.ingsw.gc28.model.cards.utils;

import it.polimi.ingsw.gc28.model.cards.CardGold;
import it.polimi.ingsw.gc28.model.cards.CardInitial;
import it.polimi.ingsw.gc28.model.cards.CardObjective;
import it.polimi.ingsw.gc28.model.cards.CardResource;
import it.polimi.ingsw.gc28.model.challenge.utils.ChallengeType;
import it.polimi.ingsw.gc28.model.objectives.positions.utils.GeneralPositionType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourcePrimaryType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceSpecialType;
import it.polimi.ingsw.gc28.model.resources.utils.ResourceType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.simple.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class ParsingHelper {
    public static void stringToResourceType(String[] str, ResourceType[] res){
        for(int a=0; a<res.length;a++){
            if(str[a]!= null){
                res[a] = ResourceType.valueOf(str[a]);
            }
            else{
                res[a]=null;
            }
        }
    }
    public static void stringToResourcePrimaryType(String[] str, ResourcePrimaryType[] res){
        for(int a=0; a<res.length;a++){
            if(str[a]!= null){
                res[a] = ResourcePrimaryType.valueOf(str[a]);
            }
            else{
                res[a]=null;
            }
        }
    }

    public static ImageView stringPathToImageView(String path){
        ImageView imageView = new ImageView();
        //Image image = new Image(path);
        //imageView.setImage(image);
        return null;
    }


    public static CardResource parseCardResource(JSONObject obj){
        String[] resourceCard = new String[4];
        ResourceType[] resources = new ResourceType[4];
        ImageView frontImg;
        ImageView backImg;

        resourceCard[0] = (String) obj.get("vertexOne");
        resourceCard[1] = (String) obj.get("vertexTwo");
        resourceCard[2] = (String) obj.get("vertexThree");
        resourceCard[3] = (String) obj.get("vertexFour");
        String resourcePrimary = (String) obj.get("resourcePrimary");
        String pointsPerPlay = (String) obj.get("pointsPerPlay");

        ParsingHelper.stringToResourceType(resourceCard, resources);
        ResourcePrimaryType resPrimary = ResourcePrimaryType.valueOf(resourcePrimary);
        int points = Integer.parseInt(pointsPerPlay);

        String frontImgPath = (String) obj.get("frontImgPath");
        String backImgPath = (String) obj.get("backImgPath");

        frontImg = stringPathToImageView(frontImgPath);
        backImg = stringPathToImageView(backImgPath);

        String id = (String) obj.get("id");

        return new CardResource(id, resources, resPrimary, points, frontImg, backImg);
    }


    public static CardGold parseGoldCard(JSONObject obj){
        String[] resourceCard = new String[4];
        String[] resourceNeeded = new String[5];
        ResourceType[] resources = new ResourceType[4];
        ResourcePrimaryType[] resNeeded = new ResourcePrimaryType[5];
        ChallengeType cha;
        ResourceSpecialType resChallenge;
        ImageView frontImg;
        ImageView backImg;

        resourceCard[0] = (String) obj.get("vertexOne");
        resourceCard[1] = (String) obj.get("vertexTwo");
        resourceCard[2] = (String) obj.get("vertexThree");
        resourceCard[3] = (String) obj.get("vertexFour");
        String resourcePrimary = (String) obj.get("resourcePrimary");
        String pointsPerPlay = (String) obj.get("pointsPerPlay");
        resourceNeeded[0] = (String) obj.get("resourceNeededOne");
        resourceNeeded[1] = (String) obj.get("resourceNeededTwo");
        resourceNeeded[2] = (String) obj.get("resourceNeededThree");
        resourceNeeded[3] = (String) obj.get("resourceNeededFour");
        resourceNeeded[4] = (String) obj.get("resourceNeededFive");
        String challenge= (String) obj.get("challenge");
        String resourceChallenge = (String) obj.get("resourceChallenge");

        ParsingHelper.stringToResourceType(resourceCard, resources);
        ResourcePrimaryType resPrimary = ResourcePrimaryType.valueOf(resourcePrimary);
        int points = Integer.parseInt(pointsPerPlay);
        ParsingHelper.stringToResourcePrimaryType(resourceNeeded, resNeeded);
        if(challenge!= null){
            cha = ChallengeType.valueOf(challenge);
        }
        else{
            cha = null;
        }
        if(resourceChallenge!= null){
            resChallenge = ResourceSpecialType.valueOf(resourceChallenge);
        }
        else{
            resChallenge = null;
        }

        String frontImgPath = (String) obj.get("frontImgPath");
        String backImgPath = (String) obj.get("backImgPath");

        frontImg = stringPathToImageView(frontImgPath);
        backImg = stringPathToImageView(backImgPath);

        String id = (String) obj.get("id");

        return new CardGold(id, resources, resPrimary, points, resNeeded, cha, resChallenge, frontImg, backImg);
    }

    public static CardInitial parseCardInitial(JSONObject obj){
        String[] resourceBack = new String[4];
        String[] resourceFront = new String[4];
        String[] resourceCenter = new String[3];

        ResourceType[] resBack = new ResourceType[4];
        ResourceType[] resFront = new ResourceType[4];
        ResourceType[] resCenter = new ResourceType[3];

        ImageView frontImg;
        ImageView backImg;

        resourceBack[0] = (String) obj.get("vertexBackOne");
        resourceBack[1] = (String) obj.get("vertexBackTwo");
        resourceBack[2] = (String) obj.get("vertexBackThree");
        resourceBack[3] = (String) obj.get("vertexBackFour");
        resourceFront[0] = (String) obj.get("vertexFrontOne");
        resourceFront[1] = (String) obj.get("vertexFrontTwo");
        resourceFront[2] = (String) obj.get("vertexFrontThree");
        resourceFront[3] = (String) obj.get("vertexFrontFour");
        resourceCenter[0] = (String) obj.get("centralResourceOne");
        resourceCenter[1] = (String) obj.get("centralResourceTwo");
        resourceCenter[2] = (String) obj.get("centralResourceThree");

        ParsingHelper.stringToResourceType(resourceBack, resBack);
        ParsingHelper.stringToResourceType(resourceFront, resFront);
        ParsingHelper.stringToResourceType(resourceCenter, resCenter);

        String frontImgPath = (String) obj.get("frontImgPath");
        String backImgPath = (String) obj.get("backImgPath");

        frontImg = stringPathToImageView(frontImgPath);
        backImg = stringPathToImageView(backImgPath);

        String id = (String) obj.get("id");

        return new CardInitial(id, resBack, resFront, resCenter, frontImg, backImg);
    }

    public static CardObjective parseObjectivePositional(JSONObject obj){
        String[] resourcePosition = new String[3];
        ResourcePrimaryType[] resPosition = new ResourcePrimaryType[3];

        ImageView frontImg;
        ImageView backImg;

        String pointsCard = (String) obj.get("points");
        String positionType = (String) obj.get("positionType");
        resourcePosition[0] = (String) obj.get("resourcePrimaryOne");
        resourcePosition[1] = (String) obj.get("resourcePrimaryTwo");
        resourcePosition[2] = (String) obj.get("resourcePrimaryThree");

        int points = Integer.parseInt(pointsCard);
        GeneralPositionType posType = GeneralPositionType.valueOf(positionType);
        ParsingHelper.stringToResourcePrimaryType(resourcePosition, resPosition);

        String id = (String) obj.get("id");

        String frontImgPath = (String) obj.get("frontImgPath");
        String backImgPath = (String) obj.get("backImgPath");

        frontImg = stringPathToImageView(frontImgPath);
        backImg = stringPathToImageView(backImgPath);

        return new CardObjective(id, posType, points, resPosition, frontImg, backImg);
    }

    public static CardObjective parseObjectiveResources(JSONObject obj){
        String[] resourceNeeded = new String[3];
        ResourceType[] resNeeded = new ResourceType[3];

        ImageView frontImg;
        ImageView backImg;

        String pointsCard = (String) obj.get("points");
        resourceNeeded[0] = (String) obj.get("resourceNeededOne");
        resourceNeeded[1] = (String) obj.get("resourceNeededTwo");
        resourceNeeded[2] = (String) obj.get("resourceNeededThree");

        int points = Integer.parseInt(pointsCard);
        ParsingHelper.stringToResourceType(resourceNeeded, resNeeded);

        String id = (String) obj.get("id");

        String frontImgPath = (String) obj.get("frontImgPath");
        String backImgPath = (String) obj.get("backImgPath");

        frontImg = stringPathToImageView(frontImgPath);
        backImg = stringPathToImageView(backImgPath);

        return new CardObjective(id, points, resNeeded, frontImg, backImg);
    }
}
