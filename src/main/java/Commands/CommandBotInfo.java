package main.java.Commands;

import main.java.Main;
import main.java.services.CommandService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;

public class CommandBotInfo extends CommandBase
{

    public CommandBotInfo(String commandIn, String usageIn){
        super(commandIn, usageIn, "Gives an embedded message with all the bot details.");
    }

    @Override
    public boolean execute(Message message, CommandService service){
        EmbedBuilder eBuild = new EmbedBuilder();
        eBuild.setTitle("Bot information.").setDescription("Here's the bot's current configurations and data:");
        eBuild.setImage(Main.getResources().bot.getSelfUser().getAvatarUrl());
        eBuild.addField("Launch Config", "-----------", false);
        for (String configSet: new ArrayList<String>(Main.getResources().botAdministratorConfig.keySet())) {
            if(configSet.startsWith("#")) {
                eBuild.addField(configSet, "#", true);
            } else {
                eBuild.addField(configSet, Main.getResources().botAdministratorConfig.get(configSet), true);
            }
        }
        eBuild.addField("Bot information", "-----------", false);
        eBuild.addField("Identification", Main.getResources().bot.getSelfUser().getName()+"#"+Main.getResources().bot.getSelfUser().getDiscriminator(), true);
        eBuild.addField("Ping", Main.getResources().bot.getPing()+"ms", true);
        eBuild.addField("Command Count", String.valueOf(Main.getResources().commands.size()), true).setColor(Color.orange);

        message.getTextChannel().sendMessage(eBuild.build()).queue();
        return true;
    }

}
