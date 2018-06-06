package main.java.Commands;

import main.java.Main;
import main.java.services.CommandService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandOccupyThread extends CommandBase
{

    public CommandOccupyThread(String commandIn, String usageIn){
        super(commandIn, usageIn, "Occupies Thread.");
    }

    @Override
    public boolean execute(Message message, CommandService service){

        List<String> botMods = (List<String>) Main.getResources().botAdministratorConfig.get("bot-maintainers");

        if(!(botMods.contains(message.getAuthor().getId()))){
            message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Permission Error").setDescription("This command requires the permission 'CUSTOM_MAINTAINER'").setImage("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/139/warning-sign_26a0.png").setColor(Color.ORANGE).build()).queue();
            return false;
        }
        while(Main.getResources().killInitiated == 0){
            try {
                synchronized (this) {
                    this.wait(1000);
                }
            } catch (Exception err){
                Main.getResources().coreService.SendErrorToHome("Command Execution Error", "An exception occured during the process.", "CommandProcessor#" + Main.getResources().services.indexOf(this));
                return false;
            }
        }
        return true;
    }

}
