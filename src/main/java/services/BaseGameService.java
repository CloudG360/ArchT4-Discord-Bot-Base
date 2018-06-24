package main.java.services;

import com.carrotsearch.sizeof.RamUsageEstimator;
import main.java.ClassTypes.OfflineMessage;
import main.java.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
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

    //User Viewable info
    protected String dispName;
    protected String desc;
    protected String icon;

    //Lobby data
    protected TextChannel lobbyJoinChannel;
    protected TextChannel lobbyChannel;
    protected Message joinMessage;
    protected List<User> lobbyUsers;
    protected int lobbyMinPlayers;
    protected int maxLobbySize;

    public int timerStart;
    public int timer;

    //General Data
    protected String gameStatus;


    public BaseGameService(String id, String displayName, String description, String iconURL, int minLobbyStart, int maxLobbySize, int timerSize){
        gameID = id;
        lobbyID = Main.getResources().gameLobbies.size() + 1;
        idUnique = gameID+"#"+lobbyID;

        timerStart = timerSize;
        timer = timerStart;
    }


    public void prepare(Guild guild, Category gameCat){
        server = guild;
        idUnique = gameID+"#"+lobbyID+"#"+server.getId();

        gameCatagory = gameCat;

    }

    @Override
    public void run(){
        beginGameScript();
    }

    protected void lobbyScript(){
        gameStatus = "LOBBY-NEP";


        while(gameStatus.startsWith("LOBBY")){
            try {
                TimeUnit.SECONDS.wait(1);

                if (Main.getResources().killInitiated == 0) {
                    return;
                }
                if (timer <= 0) {
                    return;
                }
                if (lobbyUsers.size() >= lobbyMinPlayers) {
                    gameStatus = "LOBBY-STARTING";
                    timer--;
                    lobbyChannel.getManager().setTopic(":pencil: Lobby " + idUnique + "| Status: :clock3: Game Starting Soon ("+timer+"s) | Players: "+lobbyUsers.size()).complete();

                }
                if (lobbyUsers.size() < lobbyMinPlayers) {
                    lobbyChannel.getManager().setTopic(":pencil: Lobby: " + idUnique + "| Status: Not Enough Players | Players:" + lobbyUsers.size()).complete();
                }

            } catch (Exception err){
                lobbyChannel.sendMessage(new EmbedBuilder().setTitle("Lobby Error").setDescription("An error occured during countdown.").setColor(Color.blue).setImage("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/141/warning-sign_26a0.png").build()).queue();
            }
        }
    }


    protected void beginGameScript(){

        lobbyChannel = (TextChannel) gameCatagory.createTextChannel("lobby-"+gameID.toLowerCase()+lobbyID).complete();

        while(lobbyUsers.size() < lobbyMinPlayers){
            try {
                lobbyScript();
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception err){
                return;
            }
        }

        if(Main.getResources().killInitiated==0){

        }




        //create game specific role
        //create channel limited to that role
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
