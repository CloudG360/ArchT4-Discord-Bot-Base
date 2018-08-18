package main.java.Commands;

import main.java.Main;
import main.java.services.CommandService;
import main.java.services.DataService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CommandDebugDB extends CommandBase
{

    public CommandDebugDB(String commandIn, String usageIn){
        super(commandIn, usageIn, "Occupies Thread.");
    }

    @Override
    public boolean execute(Message message, CommandService service){

        List<String> botMods = (List<String>) Main.getResources().botAdministratorConfig.get("bot-maintainers");

        if(!(botMods.contains(message.getAuthor().getId()))){
            message.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Permission Error").setDescription("This command requires the permission 'CUSTOM_MAINTAINER'").setImage("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/139/warning-sign_26a0.png").setColor(Color.ORANGE).build()).queue();
            return false;
        }

        Main.getResources().coreService.SendInfoToHome("Debug Command Initiated.", "Testing mySQL Databases", "Sent by "+message.getAuthor().getAsMention());

        DataService dserv = new DataService();

        Main.getResources().coreService.SendInfoToHome("DB TEST [STAGE 1/4]", "Testing mySQL Database INSERT", "Sent by "+message.getAuthor().getAsMention());

        List<String> columns = new ArrayList<>();
        columns.add("val1");
        columns.add("val2");
        columns.add("val3");
        List<String> values = new ArrayList<>();
        values.add("Hi.");
        values.add("Welcome to...");
        values.add("My Crib.");

        dserv.insertEntry("debug", "testdb", "testdb_entry_1",columns, values);
        dserv.insertEntry("debug", "testdb", "testdb_entry_2",columns, values);
        dserv.insertEntry("debug", "testdb", "testdb_entry_3",columns, values);

        Main.getResources().coreService.SendInfoToHome("DB TEST [STAGE 2/4]", "Testing mySQL Database READ [1/2]", "Sent by "+message.getAuthor().getAsMention());

        ResultSet rs1 = dserv.retriveEntry("debug", "dbtest", "testdb_entry_1");
        ResultSet rs2 = dserv.retriveEntry("debug", "dbtest", "testdb_entry_1");
        ResultSet rs3 = dserv.retriveEntry("debug", "dbtest", "testdb_entry_1");

        if(rs1 == null || rs2 == null || rs3 == null){
            Main.getResources().coreService.SendInfoToHome("DB TEST [STAGE 2/4]", "READ test FAILURE [1/2]", "Sent by "+message.getAuthor().getAsMention());
        }

        Main.getResources().coreService.SendInfoToHome("DB TEST [STAGE 3/4]", "Testing mySQL Database EDIT", "Sent by "+message.getAuthor().getAsMention());

        boolean s1 = dserv.editEntry("debug", "dbtest", "testdb_entry_1", "val1", "test");
        boolean s2 = dserv.editEntry("debug", "dbtest", "testdb_entry_1", "val2", "test2");
        boolean s3 = dserv.editEntry("debug", "dbtest", "testdb_entry_1", "val3", "test3");

        if(!s1||!s2 ||!s3){
            Main.getResources().coreService.SendInfoToHome("DB TEST [STAGE 3/4]", "EDIT test FAILURE", "Sent by "+message.getAuthor().getAsMention());
        }

        Main.getResources().coreService.SendInfoToHome("DB TEST [STAGE 4/4]", "Testing mySQL Database READ [2/2]", "Sent by "+message.getAuthor().getAsMention());

        ResultSet rs4 = dserv.retriveEntry("debug", "testdb", "testdb_entry_1");

        if(rs1 == null){
            Main.getResources().coreService.SendInfoToHome("DB TEST [STAGE 4/4]", "READ test FAILURE [2/2]", "Sent by "+message.getAuthor().getAsMention());
        }

        return true;

    }

}
