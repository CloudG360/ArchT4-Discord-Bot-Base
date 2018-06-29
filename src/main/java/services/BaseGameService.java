package main.java.services;

import com.carrotsearch.sizeof.RamUsageEstimator;
import main.java.ClassTypes.OfflineMessage;
import main.java.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.emote.update.EmoteUpdateNameEvent;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.RoleManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class BaseGameService extends Thread{

    //Format - gameID#lobbyId
    protected String idUnique;

    protected String gameID;
    protected int lobbyID;
    protected Guild server;
    protected Category gameCatagory;

    //In Game
    public List<TextChannel> gameChannels;
    public HashMap<String, Role> gameroles;

    //User Viewable info
    protected String dispName;
    protected String desc;
    protected String icon;

    //Lobby data
    protected TextChannel lobbyJoinChannel;
    protected TextChannel lobbyChannel;
    protected Message joinMessage;
    public Role lobbyRole;
    protected List<User> lobbyUsers;
    protected int lobbyMinPlayers;
    protected int maxLobbySize;

    public int timerStart;
    public int timer;

    //General Data
    protected String gameStatus;


    public BaseGameService(String id, int minLobbyStart, int maxLobSize, int timerSize){
        gameID = id;


        lobbyID = Main.getResources().gameLobbies.size() + 1;
        idUnique = gameID+"-"+lobbyID;

        lobbyMinPlayers = minLobbyStart;
        maxLobbySize = maxLobSize;

        timerStart = timerSize;
        timer = timerStart;

        lobbyUsers = new ArrayList<>();
        gameChannels = new ArrayList<>();
        gameroles = new HashMap<>();

        desc = "Welcome to a TEST lobby! This is a non-permanent game designed to allow developers to test the play script.";
        dispName = "TEST_LOBBY";
        icon = "https://emojipedia-us.s3.amazonaws.com/thumbs/120/google/119/construction-worker_1f477.png";
    }


    public void prepare(Guild guild, Category gameCat){
        server = guild;
        gameCatagory = gameCat;


    }

    @Override
    public void run(){
        Main.getResources().gameLobbies.add(this);

        beginGameScript();
    }

    protected void lobbyScript(){
        gameStatus = "LOBBY-NEP";

        lobbyJoinChannel = (TextChannel) gameCatagory.createTextChannel("join-"+idUnique).complete();
        lobbyJoinChannel.getPermissionOverride(server.getPublicRole()).getManager().grant(Permission.VIEW_CHANNEL).complete();
        lobbyJoinChannel.getPermissionOverride(server.getPublicRole()).getManager().grant(Permission.MESSAGE_READ).complete();

        lobbyChannel = (TextChannel) gameCatagory.createTextChannel("lobby-"+gameID.toLowerCase()+lobbyID).complete();
        GuildController gc = new GuildController(server);
        Role pRole = gc.createRole().setColor(Color.green).setName("LOBBY-"+idUnique).setMentionable(false).complete();
        lobbyChannel.createPermissionOverride(pRole).setAllow(Permission.VIEW_CHANNEL).setAllow(Permission.MESSAGE_READ).complete();

        lobbyJoinChannel.sendMessage(new EmbedBuilder().setTitle("Lobby for "+dispName).setDescription("React to join!").setImage(icon).setColor(Color.blue).build()).complete().addReaction("\u2705").complete();

        lobbyRole = pRole;


        while(gameStatus.startsWith("LOBBY")){
            try {
                TimeUnit.SECONDS.sleep(1);

                if (Main.getResources().killInitiated == 1) {
                    System.out.println("LOBBY-KILL");
                    return;
                }
                if (timer <= 0) {
                    if (lobbyUsers.size() >= lobbyMinPlayers) {
                        System.out.println("LOBBY-TIMER_ZERO");
                        lobbyJoinChannel.delete().complete();
                        lobbyChannel.delete().complete();
                        lobbyChannel=null;
                        lobbyJoinChannel=null;
                        return;
                    }
                }
                if (lobbyUsers.size() >= lobbyMinPlayers) {
                    gameStatus = "LOBBY-STARTING";
                    System.out.println("LOBBY-COUNTDOWN");
                    timer--;
                    lobbyChannel.getManager().setTopic(":pencil: Lobby " + idUnique + "| Status: :clock3: Game Starting Soon ("+timer+"s) | Players: "+lobbyUsers.size()).complete();

                }
                if (lobbyUsers.size() < lobbyMinPlayers) {
                    System.out.println("LOBBY-NEP");
                    gameStatus = "LOBBY-NEP";
                    lobbyChannel.getManager().setTopic(":pencil: Lobby: " + idUnique + "| Status: Not Enough Players | Players:" + lobbyUsers.size()).complete();
                }

            } catch (InterruptedException err){
                lobbyChannel.sendMessage(new EmbedBuilder().setTitle("Lobby Error").setDescription("An error occured during countdown.").setColor(Color.blue).setImage("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/141/warning-sign_26a0.png").build()).queue();
            }
        }
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

            while(true){ }

        }




        //create game specific role
        //create channel limited to that role
    }

    public void killGame(){
        for(Role role: gameroles.values()){
            role.delete().complete();
        }
        gameroles = null;

        for(TextChannel channel:gameChannels){
            channel.delete().complete();
        }
        gameChannels = null;

        lobbyChannel.delete().complete();
        lobbyJoinChannel.delete().complete();

        this.interrupt();
    }

    //uniqueID components
    public int getLobbyID() {
        return lobbyID;
    }

    public String getGameID() {
        return gameID;
    }

    //Info
    public String getDisplayName() {
        return dispName;
    }

    public String getDesc() {
        return desc;
    }

    public String getIconURL() {
        return icon;
    }

    @Override
    public String toString() {
        return idUnique;
    }

    //Lobby
    public int getLobbyMinSize() {
        return lobbyMinPlayers;
    }

    public int getMaxLobbySize() {
        return maxLobbySize;
    }
}
