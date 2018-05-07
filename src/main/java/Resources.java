package main.java;

import net.dv8tion.jda.core.OnlineStatus;

import java.util.HashMap;
import java.util.Map;

public class Resources {

    public Map<String, String> botAdministratorConfig;
    public Map<String, OnlineStatus> onlineStatus;


    public Resources(){
        botAdministratorConfig = new HashMap<>();

        onlineStatus = new HashMap<>();
        onlineStatus.put("ONLINE", OnlineStatus.ONLINE);
        onlineStatus.put("IDLE", OnlineStatus.IDLE);
        onlineStatus.put("DND", OnlineStatus.DO_NOT_DISTURB);
        onlineStatus.put("OFFLINE", OnlineStatus.INVISIBLE);
    }

}
