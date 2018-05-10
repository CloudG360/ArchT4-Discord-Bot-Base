package main.java.Commands;

import main.java.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

public class CommandDM extends CommandBase
{

    public CommandDM(String commandIn, String usageIn){
        super(commandIn, usageIn, "Occupies Thread.");
    }

    @Override
    public boolean execute(Message message){

        String[] content = message.getContentRaw().split(" ", 3);
        String[] contentSplit = content[2].split("-cut-", 3);

        EmbedBuilder eBuild = new EmbedBuilder().setTitle("\uD83D\uDCF0 "+contentSplit[0]).setDescription("'"+contentSplit[1]+"'").setFooter("Sent by "+message.getAuthor().getAsMention(), message.getAuthor().getAvatarUrl()).setColor(Color.CYAN);

        MessageEmbed embed = eBuild.build();

        Main.getResources().coreService.SendEmbedToHome(embed);

        message.getMentionedUsers().get(0).openPrivateChannel().complete().sendMessage(embed).queue();

        return true;
    }

}
