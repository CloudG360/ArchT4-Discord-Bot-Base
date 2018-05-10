package main.java.Commands;

import main.java.Main;
import main.java.services.CommandService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class CommandBase
{

    public static String command = "ping";
    public static String usage = "/ping";
    public static String description = "Generic Description.";

    public CommandBase(String commandIn, String usageIn){
        command = commandIn;
        usage = usageIn;
        description = "Generic Description.";
    }
    public boolean execute(Message message){

        message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Pong!").setDescription("Took "+Main.getResources().bot.getPing()+"ms").build()).queue();


    }

}
