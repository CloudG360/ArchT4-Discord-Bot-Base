package main.java.services;

import main.java.Main;
import main.java.Resources;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.restaction.pagination.AuditLogPaginationAction;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CoreService extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().startsWith(Main.getResources().prefix)) {
            CommandService cmdServiceNew = new CommandService();
            cmdServiceNew.queueTask(event.getMessage());
            cmdServiceNew.start();
            SendDebugToHome("Started Thread", "Started a new command service - CommandService#" + Main.getResources().commandServices.indexOf(cmdServiceNew), "-");
        }
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {

        AuditLogEntry auditLog = event.getGuild().getAuditLogs().complete().get(1);

        SendInfoToHome("Action 'DELETE'", "Performed in " + event.getChannel().getName() + " by " + auditLog.getUser().getAsMention() + " - `" + auditLog.getReason() + "`", "CoreThread#0");

    }


    public void SendErrorToHome(String title, String content, String location){
        EmbedBuilder embed = new EmbedBuilder();

        MessageEmbed result = embed.setTitle("ERROR: "+title).setDescription(content).setFooter(location + " | " + LocalDateTime.now().toString(), "https://imgur.com/mfj5mmJ.png").setColor(Color.red).build();
        Main.getResources().bot.getTextChannelById(Main.getResources().botAdministratorConfig.get("home-logs").toString()).sendMessage(result).queue();

    }

    public void SendInfoToHome(String title, String content, String location){
        EmbedBuilder embed = new EmbedBuilder();

        MessageEmbed result = embed.setTitle(title).setDescription(content).setFooter(location + " | " + LocalDateTime.now().toString(), "https://imgur.com/mfj5mmJ.png").setColor(Color.cyan).build();
        Main.getResources().bot.getTextChannelById(Main.getResources().botAdministratorConfig.get("home-logs").toString()).sendMessage(result).queue();

    }

    public void SendWarnToHome(String title, String content, String location){
        EmbedBuilder embed = new EmbedBuilder();

        MessageEmbed result = embed.setTitle("WARN: "+title).setDescription(content).setFooter(location + " | " + LocalDateTime.now().toString(), "https://imgur.com/mfj5mmJ.png").setColor(Color.yellow).build();
        Main.getResources().bot.getTextChannelById(Main.getResources().botAdministratorConfig.get("home-logs").toString()).sendMessage(result).queue();

    }
    public void SendDebugToHome(String title, String content, String location){
        if(Main.getResources().botAdministratorConfig.get("debug").equals("true")) {
            EmbedBuilder embed = new EmbedBuilder();

            MessageEmbed result = embed.setTitle("DEBUG: " + title).setDescription(content).setFooter(location + " | " + LocalDateTime.now().toString(), "https://imgur.com/mfj5mmJ.png").setColor(Color.gray).build();
            Main.getResources().bot.getTextChannelById(Main.getResources().botAdministratorConfig.get("home-logs").toString()).sendMessage(result).queue();
        }
    }

    public void SendEmbedToHome(MessageEmbed embed){

        Main.getResources().bot.getTextChannelById(Main.getResources().botAdministratorConfig.get("home-logs").toString()).sendMessage(embed).queue();

    }
}
