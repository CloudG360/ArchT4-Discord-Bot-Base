package main.java;

import main.java.commands.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static Resources resources;

    public static Resources getResources(){
        return resources;
    }

    public static void main(String args[]){
        resources = new Resources();

        //Bot Config
        if(new File("botconfig.txt").exists()) {
            File file = new File("botconfig.txt");

            try {
                Reader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line = "";

                while((line = bufferedReader.readLine()) != null) {
                    String[] configEntry = line.split("=");

                    if (configEntry[1].startsWith("[List]")) {
                        //List

                        String[] configList = configEntry[1].substring(6).split(",");

                        List<String> data = new ArrayList<>();
                        for(String strE : configList){
                            data.add(strE);
                        }

                        getResources().botAdministratorConfig.put(configEntry[0], data);

                    } else{
                        getResources().botAdministratorConfig.put(configEntry[0], configEntry[1]);
                    }
                }

                bufferedReader.close();

            } catch (Exception err){
                System.out.println("CONFIG ACCESS ERROR - SHUTTING DOWN:\n");
                err.printStackTrace();
                System.exit(2);
            }
        } else {
            System.out.println("CONFIG NOT FOUND - SHUTTING DOWN");
            System.exit(1);
        }
        //JDA API - Bot building.
        JDABuilder jdab = new JDABuilder(AccountType.BOT);
        jdab.setToken(getResources().botAdministratorConfig.get("#token").toString());
        jdab.setAutoReconnect(true);
        jdab.setStatus(getResources().onlineStatus.get(getResources().botAdministratorConfig.get("online-status").toString()));
        jdab.setGame(Game.playing(getResources().botAdministratorConfig.get("motd").toString()));
        jdab.addEventListener(getResources().coreService);
        try{
            getResources().bot = jdab.buildBlocking();
        } catch (Exception err){
            System.out.println("An error occured during the connection between the API and the bot.");
        }

        //----commands----
        getResources().addCommand(new CommandBotInfo("botinfo","!botinfo"));
        getResources().addCommand(new CommandOccupyThread("occupy","!occupy"));
        getResources().addCommand(new CommandServiceList("services","!services"));
        getResources().addCommand(new CommandDM("dmuser","!dmuser <@user> <Title> -Cut- <Description>"));
        getResources().addCommand(new CommandKillSafe("shutdown", "!shutdown"));
        getResources().addCommand(new CommandColourChangeRole("rainbow", "!rainbow @role"));
        getResources().addCommand(new CommandStartTestGame("starttest", "!starttest"));

        //----Databases----


        getResources().coreService.SendInfoToHome("Launched Bot with 0 errors.", "Launched and set up the bot. Enable Debug mode in the config for more info.", "Main Thread#0 (Launch Thread)");

        EmbedBuilder builder = new EmbedBuilder().setTitle("DEBUG: System Variables:").setDescription("Launch configuration variables").setColor(Color.gray).setFooter("Main Thread#0 (Launch Thread)", "https://imgur.com/mfj5mmJ.png");

        for(String key: new ArrayList<String>(getResources().botAdministratorConfig.keySet())){
            if(key.startsWith("#")){
                builder.addField(key + "-","[HIDDEN]", true);

            } else {
                builder.addField(key + "-",getResources().botAdministratorConfig.get(key).toString(), true);}
        }

        if(getResources().botAdministratorConfig.get("debug").equals("true")){

            getResources().bot.getTextChannelById(getResources().botAdministratorConfig.get("home-logs").toString()).sendMessage(builder.build()).queue();
        }

        getResources().prefix = getResources().botAdministratorConfig.get("prefix").toString();

        getResources().cacheService.start();

        getResources().isActive = true;
    }

}
