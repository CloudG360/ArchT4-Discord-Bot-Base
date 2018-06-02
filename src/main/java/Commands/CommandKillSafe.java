package main.java.Commands;

import main.java.Main;
import main.java.services.CommandService;
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

        Main.getResources().killInitiated = 1;

        message.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setTitle("Killing Systems").setDescription("Killing Bot Systems in mode: [SAFE MODE]").build()).queue();

        System.exit(0);

        return true;
    }

}
