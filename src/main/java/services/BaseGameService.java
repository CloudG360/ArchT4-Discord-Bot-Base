package main.java.services;

import com.carrotsearch.sizeof.RamUsageEstimator;
import main.java.ClassTypes.OfflineMessage;
import main.java.Main;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseGameService extends Thread{

    //Format - gameID#lobbyId
    protected String idUnique;

    protected String gameID;
    protected int lobbyID;
    protected Guild server;

    //User Viewable info
    protected String dispName;
    protected String desc;
    protected String icon;

    //Lobby data
    protected TextChannel lobbyJoinChannel;
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


    public void prepare(Guild guild){
        server = guild;
        idUnique = gameID+"#"+lobbyID+"#"+server.getId();
    }

    @Override
    public void run(){
        beginGameScript();
    }

    protected void lobbyScript(){
        gameStatus = "LOBBY-NEP";
        while(gameStatus.startsWith("LOBBY")){
            if(Main.getResources().killInitiated == 0){
                return;
            }
            if(lobbyUsers.size() >= lobbyMinPlayers){
                gameStatus = "LOBBY-STARTING";
                timer--;
            }
        }
    }


    protected void beginGameScript(){
        while(lobbyUsers.size() < lobbyMinPlayers){
            lobbyScript();
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
