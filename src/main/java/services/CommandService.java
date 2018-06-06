package main.java.services;

import main.java.Commands.CommandBase;
import main.java.Main;
import main.java.Resources;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.*;

public class CommandService extends Thread {

    public Message queuedItem;

    public CommandService(){
        this.setName("CommandService");
        Main.getResources().services.add(this);
    }

    public void queueTask(Message message){
        queuedItem=message;
    }

    @Override
    public void run() {

        processCommand();
    }

    private void processCommand(){


        Message message = queuedItem;

        Main.getResources().coreService.SendDebugToHome("Command Recieved!", "Identified as: " + message.getContentRaw().toLowerCase().split(" ")[0], "CommandProcessor#" + Main.getResources().services.indexOf(this));

        String commandStart = message.getContentRaw().toLowerCase().split(" ")[0];

        for(CommandBase commandB:Main.getResources().commands){
            String commandPrefixCombined = Main.getResources().prefix+commandB.getInfo().get("command");
            if(commandPrefixCombined.equals(commandStart)){
                commandB.execute(message, this);
                if(Main.getResources().killInitiated == 0) {
                    Main.getResources().services.remove(this);
                    this.interrupt();
                }
                return;


            }
        }

        message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("⚠ Command Error!").setDescription("Command not recognised.").setColor(Color.red).setImage(Main.getResources().bot.getSelfUser().getAvatarUrl()).build()).queue();

    }

    public void discardSelf(){
        Main.getResources().coreService.SendDebugToHome("Point Reached", "#0003", "CommandKillSafe");
        Main.getResources().services.remove(this);
        Main.getResources().coreService.SendDebugToHome("Point Reached", "#0004", "CommandKillSafe");
        this.interrupt();
    }



}
