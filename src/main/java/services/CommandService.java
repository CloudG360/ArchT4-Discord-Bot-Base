package main.java.services;

import main.java.Main;
import main.java.Resources;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.*;

public class CommandService extends Thread {

    public List<Message> queuedItems;

    public CommandService(){
        Main.getResources().commandServices.add(this);
        queuedItems = new ArrayList<Message>();
    }

    public void queueTask(Message message){

    }

    @Override
    public void run() {
        processCommand();
    }

    private void processCommand(){


        Message message = queuedItems.get(0);

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
                    eBuildService.addField("CommandService#"+Main.getResources().commandServices.indexOf(cmdServ), "Currently Active", true);
                }
                message.getTextChannel().sendMessage(eBuildService.build()).queue();

            case "!occupy":
                while(true){
                    try {
                        synchronized (this) {
                            this.wait(1000);
                        }
                    } catch (Exception err){
                        Main.getResources().coreService.SendErrorToHome("Command Execution Error", "An exception occured during the process.", "CommandProcessor#" + Main.getResources().commandServices.indexOf(this));
                        return;
                    }
                }
        }

        queuedItems.remove(0);

    }



}
