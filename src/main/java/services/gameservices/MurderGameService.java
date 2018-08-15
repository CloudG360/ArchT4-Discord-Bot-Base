package main.java.services.gameservices;

import main.java.Main;
import main.java.miscclasses.games.murder.Player;
import main.java.miscclasses.games.murder.roles.MurderRole;
import main.java.miscclasses.games.murder.weapons.GenericWeapon;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.managers.GuildController;

import java.awt.*;
import java.util.Map;

public class MurderGameService extends BaseGameService {

    private boolean isDay = true;

    private boolean isTrainInStation;
    private boolean isTrainOnTrip;
    private int daysTillTrainReturn;

    private Map<String, Player> players;

    private Map<String, MurderRole> murderRoles;
    private Map<String, GenericWeapon> weapons;
    private Map<String, Player> items;

    public MurderGameService(String id, int minLobbyStart, int maxLobbySize, int timerSize, String[] bootArgs){
        super(id, minLobbyStart, maxLobbySize, timerSize, bootArgs);
        desc = "Welcome to a Murder lobby! This is a game where players try to figure out who's killing the village!";
        dispName = "Murder";
        icon = "https://imgur.com/wbhG4gB.png";
    }




    protected void beginGameScript(){


        lobbyScript();


        if(Main.getResources().killInitiated==0){

            GuildController gcontrol = new GuildController(server);
            Role playerRole = gcontrol.createRole().setColor(Color.green).setName("MAIN-"+idUnique).setMentionable(false).complete();

            TextChannel gamechannel = (TextChannel) gameCatagory.createTextChannel("test-"+idUnique).complete();
            gamechannel.createPermissionOverride(playerRole).setAllow(Permission.VIEW_CHANNEL).setAllow(Permission.MESSAGE_READ).setAllow(Permission.MESSAGE_READ).complete();
            gameChannels.add(gamechannel);

            for(User user: lobbyUsers){
                GuildController guildController = new GuildController(server);
                guildController.addRolesToMember(server.getMember(user), playerRole).complete();
            }

            gameroles.put("MAIN", playerRole);

            gameChannels.get(0).sendMessage(new EmbedBuilder().setTitle("Welcome to "+dispName).setDescription(desc).setImage(icon).build()).complete();
            gameChannels.get(0).sendMessage(new EmbedBuilder().setTitle("Just a second... ").setDescription("We're assigning roles and setting up the start of the game.").build()).complete();

            for(User user: lobbyUsers){
                GuildController guildController = new GuildController(server);
                TextChannel userChan = (TextChannel) guildController.createTextChannel(user.getName().toLowerCase()+"--"+user.getDiscriminator()).setParent(gameCatagory).complete();
                userChan.createPermissionOverride(server.getMember(user)).setAllow(Permission.ALL_TEXT_PERMISSIONS).complete();

                Player userPlayer = new Player(user, userChan, this);

                players.put(user.getName()+"#"+user.getDiscriminator(), userPlayer);

            }

            // Assign a "Game Role" to a player. Alert the players of their roles in their channels as well as updating the topic.
            // Tell players in main channel to check their channels and begin the game.

            while(true){

            }

        }




        //create game specific role
        //create channel limited to that role
    }



    public void setDay(){

    }

    public void setNight(){

    }

    public void summonTrain(){

    }

    public void sendOffTrain(){

    }

}
