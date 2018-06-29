package main.java.Commands;

import main.java.Main;
import main.java.services.CommandService;
import main.java.services.TestGameService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.List;

public class CommandStartTestGame extends CommandBase
{

    public CommandStartTestGame(String commandIn, String usageIn){
        super(commandIn, usageIn, "Starts a Test Branch server.");
    }

    @Override
    public boolean execute(Message message, CommandService service){
            TestGameService tgs = new TestGameService("test", 2, 16, 30);
            tgs.prepare(message.getGuild(), message.getGuild().getCategoryById("455101945447710731"));
            tgs.start();
        return true;
    }

}
