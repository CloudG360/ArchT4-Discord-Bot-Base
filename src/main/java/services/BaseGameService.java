package main.java.services;

import com.carrotsearch.sizeof.RamUsageEstimator;
import main.java.ClassTypes.OfflineMessage;
import main.java.Main;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseGameService extends Thread{

    public BaseGameService(String id, String displayName, String description, int minLobbyStart, int maxLobbySize){

    }


    @Override
    public void run(){
        lobbyScript();
        beginGameScript();
    }

    protected void lobbyScript(){
        while(Main.getResources().killInitiated == 0){

        }
    }


    protected void beginGameScript(){

    }


}
