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
        Main.getResources().commandServices.add(this);
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

        Main.getResources().coreService.SendDebugToHome("Command Recieved!", "Identified as: " + message.getContentRaw().toLowerCase().split(" ")[0], "CommandProcessor#" + Main.getResources().commandServices.indexOf(this));

        String commandStart = message.getContentRaw().toLowerCase().split(" ")[0];

        for(CommandBase commandB:Main.getResources().commands){
            String commandPrefixCombined = Main.getResources().prefix+commandB.getInfo().get("command");
            if(commandPrefixCombined.equals(commandStart)){
                commandB.execute(message, this);
                Main.getResources().commandServices.remove(this);
                this.interrupt();
                return;


            }
        }

        message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("⚠ Command Error!").setDescription("Command not recognised.").setColor(Color.red).setImage(Main.getResources().bot.getSelfUser().getAvatarUrl()).build()).queue();

    }



}
