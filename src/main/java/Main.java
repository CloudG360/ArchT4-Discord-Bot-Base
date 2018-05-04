package main.java;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

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
                    getResources().botAdministratorConfig.put(configEntry[0], configEntry[1]);
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
        jdab.setToken(getResources().botAdministratorConfig.get("token"));
        jdab.setAutoReconnect(true);
        jdab.setStatus(getResources().onlineStatus.get(getResources().botAdministratorConfig.get("online-status")));
        jdab.setGame(Game.playing(getResources().botAdministratorConfig.get("motd")));
        try{
            jdab.buildBlocking();
        } catch (Exception err){
            System.out.println("An error occured during the connection between the API and the bot.");
        }
    }

}
