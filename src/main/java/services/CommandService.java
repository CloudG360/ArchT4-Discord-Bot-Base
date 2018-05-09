package main.java.services;

import main.java.Main;
import main.java.Resources;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

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

    public void queueAction(Message message) {
        if (message.getContentRaw().toLowerCase().startsWith(Main.getResources().prefix)){
                Map<String, Object> queueableAction = new HashMap<String, Object>();

            queueableAction.put("Message", message);

            queuedItems.add(queueableAction);

            if (!isLookingForRequests && !isInUse) {
                processCommand();
            }
        }
    }

    @Override
    public void run() {

        waitForRequest();

    }

    private void waitForRequest(){
        isLookingForRequests = true;

        int tries = 0;
        Main.getResources().coreService.SendDebugToHome("Awaiting Request","Awaiting request on thread. - CommandService#"+Main.getResources().commandServices.indexOf(this),"-");

        while(queuedItems.size() < 1){

            if(tries < 30){
                tries++;
                try {
                    synchronized (this) {
                        this.wait(1000);
                    }
                } catch (Exception err){
                    Main.getResources().coreService.SendErrorToHome("Command Queue Error", "Unable to ", "CommandProcessor#" + Main.getResources().commandServices.indexOf(this));
                    return;
                }
            } else {
                Main.getResources().coreService.SendDebugToHome("Closing Thread.", "Closing Thread due to lack of activity.", "CommandProcessor#" + Main.getResources().commandServices.indexOf(this));
                this.interrupt();
                return;
            }

        }

        isLookingForRequests=false;
        Main.getResources().coreService.SendDebugToHome("Processing command","Awaiting request on thread. - CommandService#"+Main.getResources().commandServices.indexOf(this),"-");
        processCommand();
    }

    private void processCommand(){
        Map<String, Object> nextAction = queuedItems.get(0);

        Message message;

        try{
            message = ((Message) nextAction.get("Message"));
        } catch (Exception err){
            Main.getResources().coreService.SendDebugToHome("Failed data request on Thread", "An error occured while requesting the 'message' data from the queue.", "CommandProcessor#" + Main.getResources().commandServices.indexOf(this));
            return;
        }

        Main.getResources().coreService.SendDebugToHome("Command Recieved!", "Identified as: " + message.getContentRaw().toLowerCase().split(" ")[0], "CommandProcessor#" + Main.getResources().commandServices.indexOf(this));

        switch (message.getContentRaw().toLowerCase().split(" ")[0]){
            case "!information":
                EmbedBuilder eBuild = new EmbedBuilder();
                eBuild.setTitle("Bot information.").setDescription("Here's the bot's current configurations and data:");
                eBuild.setImage(Main.getResources().bot.getSelfUser().getAvatarUrl());
                eBuild.addField("Launch Config", " ", false);

                for(String key: new ArrayList<String>(Main.getResources().botAdministratorConfig.keySet())){
                    if(key.startsWith("#")){
                        eBuild.addField(key + "-","[HIDDEN]", true);

                    } else {
                        eBuild.addField(key + "-", Main.getResources().botAdministratorConfig.get(key), true);}
                }

                eBuild.addBlankField(false).addField("Bot Information", "", false).addField("Name", Main.getResources().bot.getSelfUser().getName() + "#" + Main.getResources().bot.getSelfUser().getDiscriminator(), true).addField("Ping", Long.toString(Main.getResources().bot.getPing()), true);

                Main.getResources().coreService.SendDebugToHome("Command Logging", eBuild.getFields().toString(), "-");

                MessageEmbed eBuilt = eBuild.build();

                message.getTextChannel().sendMessage(eBuilt).queue();
                break;
            case "!servicelist":
                EmbedBuilder eBuildService = new EmbedBuilder();
                eBuildService.setTitle("Bot Command Services:").setDescription("Here's a list of the current threads running.");
                eBuildService.setImage(Main.getResources().bot.getSelfUser().getAvatarUrl());
                for(CommandService cmdServ:Main.getResources().commandServices){
                    eBuildService.addField("CommandService#"+Main.getResources().commandServices.indexOf(cmdServ), "isLookingForRequests="+String.valueOf(cmdServ.isLookingForRequests)+"|"+"isInUse="+String.valueOf(cmdServ.isInUse)+"|"+"queuedItems.size()="+String.valueOf(cmdServ.queuedItems.size()), true);
                }
                message.getTextChannel().sendMessage(eBuildService.build()).queue();

            case "!occupy":
                while(isInUse){
                    try {
                        synchronized (this) {
                            this.wait(1000);
                        }
                    } catch (Exception err){
                        Main.getResources().coreService.SendErrorToHome("Command Execution Error", "An exception occured during the process.", "CommandProcessor#" + Main.getResources().commandServices.indexOf(this));
                        return;
                    }
                }
                break;
        }

        queuedItems.remove(0);
        waitForRequest();

    }



}
