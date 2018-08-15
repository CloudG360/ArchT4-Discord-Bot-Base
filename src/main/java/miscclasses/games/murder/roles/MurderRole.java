package main.java.miscclasses.games.murder.roles;

import main.java.miscclasses.games.murder.Player;

import java.awt.*;
import java.util.Map;

public abstract class MurderRole {

    private static String id = "XXX";
    private static String description = "Something must've gone wrong!  Anyway... Easter Egg. You will be treated as a murderer but will start with a weapon! A developer weapon!";

    private static Color colour = Color.DARK_GRAY;

    public static String getId() {
        return id;
    }

    public static String getDescription() {
        return description;
    }

    public static Color getColour() {
        return colour;
    }

    public static Map<String, String> getMainAbility(){
        return null;
    }

    public static void useMainAbility(Player player, Player targetPlayer){

    }

    public static Map<String, String> getSecondaryAbility(){
        return null;
    }

    public static void useSecondaryAbility(Player player, Player targetPlayer){

    }

}
