package main.java.services.gameservices;

import main.java.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class BaseGameService extends Thread{

    //Format - gameID#lobbyId
    protected String idUnique;

    protected String gameID;
    protected int lobbyID;
    protected Guild server;
    protected Category gameCatagory;

    //These are variables intended only intended to be changed by the
    //Divided up by ";" - Format: ARG-NAME=VALUE; - Classes which extend this can look for additional args.
    protected String[] args;

    protected boolean lobbyTimer = true;
    protected boolean eventMode = false;
    protected String gameType = "main";

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

    //Logging
    public List<String> presetupErrors;


    public BaseGameService(String id, int minLobbyStart, int maxLobSize, int timerSize, String[] bootArgs){

        args = bootArgs;
        presetupErrors = new ArrayList<>();

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

        for (String bootArg: args) {
            if(bootArg.startsWith("minPlayerLimit")){
                if(bootArg.split("=").length==2){
                    lobbyMinPlayers=Integer.parseInt(bootArg.split("=")[1]);
                }else if(bootArg.split("=").length==1){
                    lobbyMinPlayers=1;
                } else {
                    presetupErrors.add("ArgumentError; Incorrect usage of the 'minPlayerLimit' argument.");
                }
            }

            if(bootArg.startsWith("maxPlayerLimit")){
                if(bootArg.split("=").length==2){
                    maxLobbySize=Integer.parseInt(bootArg.split("=")[1]);
                }else if(bootArg.split("=").length==1){
                    maxLobbySize=100000;
                } else {
                    presetupErrors.add("ArgumentError; Incorrect usage of the 'maxPlayerLimit' argument.");
                }
            }
        }
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

    @Override
    public String toString() {
        return idUnique;
    }

    public String getIdUnique() {
        return idUnique;
    }

    public void setIdUnique(String idUnique) {
        this.idUnique = idUnique;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public int getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(int lobbyID) {
        this.lobbyID = lobbyID;
    }

    public Guild getServer() {
        return server;
    }

    public void setServer(Guild server) {
        this.server = server;
    }

    public Category getGameCatagory() {
        return gameCatagory;
    }

    public void setGameCatagory(Category gameCatagory) {
        this.gameCatagory = gameCatagory;
    }

    public List<TextChannel> getGameChannels() {
        return gameChannels;
    }

    public void setGameChannels(List<TextChannel> gameChannels) {
        this.gameChannels = gameChannels;
    }

    public HashMap<String, Role> getGameroles() {
        return gameroles;
    }

    public void setGameroles(HashMap<String, Role> gameroles) {
        this.gameroles = gameroles;
    }

    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public TextChannel getLobbyJoinChannel() {
        return lobbyJoinChannel;
    }

    public void setLobbyJoinChannel(TextChannel lobbyJoinChannel) {
        this.lobbyJoinChannel = lobbyJoinChannel;
    }

    public TextChannel getLobbyChannel() {
        return lobbyChannel;
    }

    public void setLobbyChannel(TextChannel lobbyChannel) {
        this.lobbyChannel = lobbyChannel;
    }

    public Message getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(Message joinMessage) {
        this.joinMessage = joinMessage;
    }

    public Role getLobbyRole() {
        return lobbyRole;
    }

    public void setLobbyRole(Role lobbyRole) {
        this.lobbyRole = lobbyRole;
    }

    public List<User> getLobbyUsers() {
        return lobbyUsers;
    }

    public void setLobbyUsers(List<User> lobbyUsers) {
        this.lobbyUsers = lobbyUsers;
    }

    public int getLobbyMinPlayers() {
        return lobbyMinPlayers;
    }

    public void setLobbyMinPlayers(int lobbyMinPlayers) {
        this.lobbyMinPlayers = lobbyMinPlayers;
    }

    public int getMaxLobbySize() {
        return maxLobbySize;
    }

    public void setMaxLobbySize(int maxLobbySize) {
        this.maxLobbySize = maxLobbySize;
    }

    public int getTimerStart() {
        return timerStart;
    }

    public void setTimerStart(int timerStart) {
        this.timerStart = timerStart;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }
}
