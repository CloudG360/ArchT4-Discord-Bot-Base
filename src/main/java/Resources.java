package main.java;

import main.java.commands.CommandBase;
import main.java.services.gameservices.BaseGameService;
import main.java.services.CacheService;
import main.java.services.CommandService;
import main.java.services.CoreService;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resources {

    public JDA bot;

    public Map<String, Object> botAdministratorConfig;
    public Map<String, OnlineStatus> onlineStatus;

    public List<Thread> services;
    public List<CommandBase> commands;

    public List<BaseGameService> gameLobbies;

    public CoreService coreService;

    public CacheService cacheService;

    public String prefix;

    public int killInitiated;
    public CommandService commandKillParent;

    public boolean isActive;


    public Resources(){
        botAdministratorConfig = new HashMap<String, Object>();

        prefix = "!";

        onlineStatus = new HashMap<>();
        onlineStatus.put("ONLINE", OnlineStatus.ONLINE);
        onlineStatus.put("IDLE", OnlineStatus.IDLE);
        onlineStatus.put("DND", OnlineStatus.DO_NOT_DISTURB);
        onlineStatus.put("OFFLINE", OnlineStatus.INVISIBLE);

        services  = new ArrayList<>();
        commands  = new ArrayList<>();
        gameLobbies = new ArrayList<>();

        coreService = new CoreService();

        cacheService = new CacheService();

        killInitiated = 0;

        isActive = false;
    }

    public void addCommand(CommandBase command){
        commands.add(command);
    }

}
