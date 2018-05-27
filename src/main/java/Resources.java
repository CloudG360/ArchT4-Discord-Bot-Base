package main.java;

import main.java.Commands.CommandBase;
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

    public List<CommandService> commandServices;
    public List<CommandBase> commands;

    public CoreService coreService;

    public String prefix;


    public Resources(){
        botAdministratorConfig = new HashMap<String, Object>();

        prefix = "!";

        onlineStatus = new HashMap<>();
        onlineStatus.put("ONLINE", OnlineStatus.ONLINE);
        onlineStatus.put("IDLE", OnlineStatus.IDLE);
        onlineStatus.put("DND", OnlineStatus.DO_NOT_DISTURB);
        onlineStatus.put("OFFLINE", OnlineStatus.INVISIBLE);

        commandServices  = new ArrayList<>();
        commands  = new ArrayList<>();

        coreService = new CoreService();
    }

    public void addCommand(CommandBase command){
        commands.add(command);
    }

}
