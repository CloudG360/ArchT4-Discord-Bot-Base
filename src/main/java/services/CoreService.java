package main.java.services;

import main.java.miscclasses.OfflineMessage;
import main.java.Main;
import main.java.services.gameservices.BaseGameService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;


import java.time.LocalDateTime;
import java.awt.Color;
import java.util.List;
import java.util.Map;

public class CoreService extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(Main.getResources().isActive) {
            if(event.getMessage().getEmbeds().size() < 1) {
                ((Map<String, OfflineMessage>) Main.getResources().cacheService.cacheTree.get("message-cache")).put(event.getMessageId(), new OfflineMessage(event.getMessage().getAuthor(), event.getMessage().getMentionedRoles(), event.getMessage().getContentRaw(), event.getChannel()));
            }
            if (event.getMessage().getContentRaw().startsWith(Main.getResources().prefix)) {
                CommandService cmdServiceNew = new CommandService();
                cmdServiceNew.queueTask(event.getMessage());
                cmdServiceNew.start();
                SendDebugToHome("Started Thread", "Started a new command service - CommandService#" + Main.getResources().services.indexOf(cmdServiceNew), "-");
            }
        }
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {

        AuditLogEntry auditLog = event.getGuild().getAuditLogs().complete().get(1);

        OfflineMessage messageHistory = ((Map<String, OfflineMessage>)Main.getResources().cacheService.cacheTree.get("message-cache")).get(event.getMessageId());

        String AuthorMention = "null";
        String MessageContenet = "null";

        SendInfoToHome("Action 'DELETE'", "Performed in " + event.getChannel().getName() + " - Message Author: " + messageHistory.getUser().getAsMention() + " - Reason: `" + auditLog.getReason() + "` Message:" + messageHistory.getMessageRaw(), "CoreThread#0 - Performed by:" + auditLog.getUser().getAsMention());

    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event){
        System.out.println("POINT 0");
        if(!event.getUser().getId().equals(Main.getResources().bot.getSelfUser().getId())){
            System.out.println("POINT 1");
            String idUnique = event.getChannel().getName().substring(5);
            for(BaseGameService service: Main.getResources().gameLobbies){
                System.out.println("POINT 2");
                System.out.println(idUnique + "/" + service.toString());
                if(service.getMaxLobbySize() > service.getLobbyUsers().size()) {
                    System.out.println("POINT 3");
                    if (service.toString().equals(idUnique)) {
                        System.out.println("POINT 4");
                        if(service.getServer() == event.getGuild()) {
                            System.out.println("POINT 5");
                            GuildController ctrl = new GuildController(event.getGuild());
                            ctrl.addRolesToMember(event.getMember(), service.lobbyRole).complete();
                            List lobbyUsers = service.getLobbyUsers();
                            lobbyUsers.add(event.getUser());
                            service.setLobbyUsers(lobbyUsers);
                            return;
                        }
                    }
                }
            }
        }
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

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
