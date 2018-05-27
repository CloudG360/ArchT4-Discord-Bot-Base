package main.java.Commands;

import main.java.Main;
import main.java.services.CommandService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommandServiceList extends CommandBase
{

    public CommandServiceList(String commandIn, String usageIn){
        super(commandIn, usageIn, "Gives an embedded message with all the bot details.");
    }

    @Override
    public boolean execute(Message message, CommandService service){

        java.util.List<String> botMods = (List<String>) Main.getResources().botAdministratorConfig.get("bot-maintainers");

        if(!(botMods.contains(message.getAuthor().getId()))){
            message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Permission Error").setDescription("This command requires the permission 'CUSTOM_MAINTAINER'").setImage("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/139/warning-sign_26a0.png").setColor(Color.ORANGE).build()).queue();
            return false;
        }

        EmbedBuilder eBuild = new EmbedBuilder();
        eBuild.setTitle("Service List").setDescription("Here's the list of currently active services");
        eBuild.setImage(Main.getResources().bot.getSelfUser().getAvatarUrl());
        for (CommandService cmdS: Main.getResources().commandServices) {
            eBuild.addField("CommandService#"+Main.getResources().commandServices.indexOf(cmdS), "Currently Active", true);
        }
        eBuild.setColor(Color.orange);

        message.getTextChannel().sendMessage(eBuild.build()).queue();
        return true;
    }

}
