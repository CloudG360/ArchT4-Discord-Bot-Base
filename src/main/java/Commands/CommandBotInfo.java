package main.java.Commands;

import com.carrotsearch.sizeof.RamUsageEstimator;
import main.java.Main;
import main.java.services.CommandService;
import main.java.services.CoreService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommandBotInfo extends CommandBase
{

    public CommandBotInfo(String commandIn, String usageIn){
        super(commandIn, usageIn, "Gives an embedded message with all the bot details.");
    }

    @Override
    public boolean execute(Message message, CommandService service){

        java.util.List<String> botMods = (List<String>) Main.getResources().botAdministratorConfig.get("bot-maintainers");

        if(!(message.getGuild().getMember(message.getAuthor()).hasPermission(Permission.MESSAGE_MANAGE))){
            message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Permission Error").setDescription("This command requires the permission 'MESSAGE_MANAGE'").setImage("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/139/warning-sign_26a0.png").setColor(Color.ORANGE).build()).queue();
            return false;
        }

        EmbedBuilder eBuild = new EmbedBuilder();
        eBuild.setTitle("Bot information.").setDescription("Here's the bot's current configurations and data:");
        eBuild.setImage(Main.getResources().bot.getSelfUser().getAvatarUrl());
        eBuild.addField("Launch Config", "-----------", false);
        for (String configSet: new ArrayList<String>(Main.getResources().botAdministratorConfig.keySet())) {
            if(configSet.startsWith("#")) {
                eBuild.addField(configSet, "#", true);
            } else {
                eBuild.addField(configSet, Main.getResources().botAdministratorConfig.get(configSet).toString(), true);
            }
        }
        eBuild.addField("Bot information", "-----------", false);
        eBuild.addField("Identification", Main.getResources().bot.getSelfUser().getName()+"#"+Main.getResources().bot.getSelfUser().getDiscriminator(), true);
        eBuild.addField("Ping", Main.getResources().bot.getPing()+"ms", true);
        eBuild.addField("Command Count", String.valueOf(Main.getResources().commands.size()), true).setColor(Color.orange);

        String currentByteCount = CoreService.humanReadableByteCount(RamUsageEstimator.sizeOf(Main.getResources().cacheService.cacheTree), true);
        long currentLimit = Long.parseLong(Main.getResources().botAdministratorConfig.get("cache-byte_limit").toString());

        String convertedLimit = CoreService.humanReadableByteCount(currentLimit,true);

        eBuild.addField("Cache", "Used `"+currentByteCount+"/"+convertedLimit+ "`", true);

        message.getTextChannel().sendMessage(eBuild.build()).queue();
        return true;
    }

}
