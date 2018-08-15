package main.java.miscclasses.games.murder.weapons;

public abstract class GenericWeapon {

    private static String id = "Killer's Blade";
    private static String description = "You're not meant to be reading this. Seeing as you are, you have an incomplete weapon. It it's stats are incomplete, you have a really OP weapon. Congrats!";

    public GenericWeapon(){

    }

    public static String getId() {
        return id;
    }

    public static String getDescription() {
        return description;
    }



}
