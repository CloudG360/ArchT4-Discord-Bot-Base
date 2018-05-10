package main.java.Commands;

import main.java.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.util.HashMap;
import java.util.Map;

public class CommandOccupyThread extends CommandBase
{

    public CommandOccupyThread(String commandIn, String usageIn){
        super(commandIn, usageIn, "Occupies Thread.");
    }

    @Override
    public boolean execute(Message message){
        while(true){
            try {
                synchronized (this) {
                    this.wait(1000);
                }
            } catch (Exception err){
                Main.getResources().coreService.SendErrorToHome("Command Execution Error", "An exception occured during the process.", "CommandProcessor#" + Main.getResources().commandServices.indexOf(this));
                return false;
            }
        }
    }

}
