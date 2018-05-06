package main.java.services;

import main.java.Main;
import net.dv8tion.jda.core.entities.Message;

public class CommandService extends Thread {

    public boolean isForcedActive;
    public boolean isInUse;

    public CommandService(boolean forceActivation){
        isInUse = false;
        isForcedActive = forceActivation;

        Main.getResources().commandServices.add(this);
    }

    public void queueAction(Message message){

    }

}
