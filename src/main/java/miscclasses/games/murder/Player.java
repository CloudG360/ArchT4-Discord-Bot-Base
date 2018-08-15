package main.java.miscclasses.games.murder;

import main.java.miscclasses.games.murder.roles.MurderRole;
import main.java.services.gameservices.MurderGameService;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class Player {

    private List<String> weaponInventory;
    private List<String> itemInventory;

    private MurderRole role;

    public Player(User user, TextChannel personalChannel, MurderGameService game){

    }

    public void setRole(MurderRole role) {
        this.role=role;
    }
}
