package main.java.Commands;

import main.java.Main;
import main.java.services.CommandService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandBase
{

    protected String command;
    protected String usage;
    protected String description;

    public CommandBase(String commandIn, String usageIn, String descriptionIn){
        this.command = commandIn;
        this.usage = usageIn;
        this.description = descriptionIn;
    }

    public boolean execute(Message message){

        message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Pong!").setDescription("Took "+Main.getResources().bot.getPing()+"ms").build()).queue();

        return true;
    }

    public Map<String,String> getInfo(){
        Map<String,String> map = new HashMap<>();
        map.put("command", command);
        map.put("usage", usage);
        map.put("description", description);
        return map;
    }

}
