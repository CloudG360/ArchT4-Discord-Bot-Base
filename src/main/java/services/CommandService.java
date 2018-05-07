package main.java.services;

import main.java.Main;
import main.java.Resources;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.util.*;

public class CommandService extends Thread {

    public boolean isForcedActive;
    public boolean isInUse;

    public boolean isLookingForRequests;

    public List<Map<String, Object>> queuedItems;

    public CommandService(boolean forceActivation){
        isInUse = false;
        isForcedActive = forceActivation;
        isLookingForRequests = false;

        queuedItems = new ArrayList<Map<String, Object>>();

        Main.getResources().commandServices.add(this);
    }

    public void queueAction(Message message){
        Map<String, Object> queueableAction = new HashMap<String, Object>();

        queueableAction.put("Message", message);

        queuedItems.add(queueableAction);

        if(!isLookingForRequests && !isInUse){
            processCommand();
        }
    }

    private void waitForRequest(){
        isLookingForRequests = true;

        int tries = 0;

        while(queuedItems.size() == 0){

            if(tries < 30){
                tries++;
                try {
                    synchronized (this) {
                        this.wait(1000);
                    }
                } catch (Exception err){
                    Main.getResources().coreService.SendErrorToHome("Command Queue Error", "Unable to ", "CommandProcessor#" + Main.getResources().commandServices.indexOf(this));
                    break;
                }
            }
        }
    }

    private void processCommand(){
        Map<String, Object> nextAction = queuedItems.get(0);

        Message message;

        try{
            message = ((Message) nextAction.get("Message"));
        } catch (Exception err){

        }
    }



}
