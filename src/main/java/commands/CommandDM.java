package main.java.commands;

import main.java.Main;
import main.java.services.CommandService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.List;

public class CommandDM extends CommandBase
{

    public CommandDM(String commandIn, String usageIn){
        super(commandIn, usageIn, "Occupies Thread.");
    }

    @Override
    public boolean execute(Message message, CommandService service){

        java.util.List<String> botMods = (List<String>) Main.getResources().botAdministratorConfig.get("bot-maintainers");

        if(!(message.getGuild().getMember(message.getAuthor()).hasPermission(Permission.MANAGE_SERVER))){
            message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Permission Error").setDescription("This command requires the permission 'MANAGE_SERVER'").setImage("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/139/warning-sign_26a0.png").setColor(Color.ORANGE).build()).queue();
            return false;
        }

        String[] content = message.getContentRaw().split(" ", 3);
        String[] contentSplit = content[2].split("-cut-", 3);

        EmbedBuilder eBuild = new EmbedBuilder().setTitle("\uD83D\uDCF0 "+contentSplit[0]).setDescription("'"+contentSplit[1]+"'").setFooter("Sent by "+message.getAuthor().getAsMention(), message.getAuthor().getAvatarUrl()).setColor(Color.CYAN);

        MessageEmbed embed = eBuild.build();

        Main.getResources().coreService.SendEmbedToHome(embed);

        try {
            if (message.getMentionedUsers().size() > 0) {
                message.getMentionedUsers().get(0).openPrivateChannel().complete().sendMessage(embed).queue();
            }

            if (message.getMentionedRoles().size() > 0) {
                for (Member member : message.getGuild().getMembersWithRoles(message.getMentionedRoles().get(0))) {
                    member.getUser().openPrivateChannel().complete().sendMessage(embed).queue();
                }
            }
        } catch (Exception err){
            message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("⚠ Command Error!").setDescription("Unable to open DM with User(s)").setColor(Color.red).setImage(Main.getResources().bot.getSelfUser().getAvatarUrl()).build()).queue();

        }

        return true;
    }

}
