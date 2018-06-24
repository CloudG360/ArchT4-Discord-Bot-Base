package main.java.Commands;

import main.java.Main;
import main.java.services.CommandService;
import main.java.services.JanitorService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandKillSafe extends CommandBase
{

    public CommandKillSafe(String commandIn, String usageIn){
        super(commandIn, usageIn, "Gives an embedded message with all the bot details.");
    }

    @Override
    public boolean execute(Message message, CommandService service){

        java.util.List<String> botMods = (List<String>) Main.getResources().botAdministratorConfig.get("bot-maintainers");

        if(!(botMods.contains(message.getAuthor().getId()))){
            message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Permission Error").setDescription("This command requires the permission 'CUSTOM_MAINTAINER'").setImage("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/139/warning-sign_26a0.png").setColor(Color.ORANGE).build()).queue();
            return false;
        }

        message.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setTitle("Initiated shutdown").setDescription("Initiated a bot-scope shutdown in STANDARD MODE. Standard mode provides 10 seconds for systems to end, before cleaning up them forcefully.").build()).queue();

        Main.getResources().commandKillParent = service;
        Main.getResources().killInitiated = 1;

        message.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setTitle("Killing Systems").setDescription("Killing Bot Systems in mode: [PRECISE MODE]").build()).complete();

        JanitorService janitorService = new JanitorService();
        janitorService.start();


        message.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setTitle("Killing Systems").setDescription("All Secondary Systems killed. Terminating primary systems.").build()).complete();

        System.exit(0);

        return true;
    }

}
