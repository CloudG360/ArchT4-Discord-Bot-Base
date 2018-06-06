package main.java.Commands;

import main.java.Main;
import main.java.services.CommandService;
import main.java.services.RoleColourService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.List;

public class CommandColourChangeRole extends CommandBase
{

    public CommandColourChangeRole(String commandIn, String usageIn){
        super(commandIn, usageIn, "Makes a role rainbow.");
    }

    @Override
    public boolean execute(Message message, CommandService service){

        List<String> botMods = (List<String>) Main.getResources().botAdministratorConfig.get("bot-maintainers");

        if(!(botMods.contains(message.getAuthor().getId()))){
            message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Permission Error").setDescription("This command requires the permission 'CUSTOM_MAINTAINER'").setImage("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/139/warning-sign_26a0.png").setColor(Color.ORANGE).build()).queue();
            return false;
        }

        if(message.getMentionedRoles().size() < 1){
            return false;
        }

        RoleColourService colourService = new RoleColourService(message.getMentionedRoles().get(0));

        colourService.start();

        message.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setTitle("Rainbows!").setDescription("Rainbowifying role!").build()).queue();

        return true;
    }

}
