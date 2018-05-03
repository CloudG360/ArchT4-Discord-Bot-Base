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

    public static void Main(String args[]){

        //Bot Config
        if(new File("botconfig.txt").exists()) {
            File file = new File("botconfig.txt");

            try {
                Reader reader = new FileReader(file);
            } catch (Exception err){
                System.out.println("CONFIG ACCESS ERROR - SHUTTING DOWN");
                System.exit(2);
            }
        } else {
            System.out.println("CONFIG NOT FOUND - SHUTTING DOWN");
            System.exit(1);
        }
        //JDA API - Bot building.
        JDABuilder jdab = new JDABuilder(AccountType.BOT);
        jdab.setToken("-");
        jdab.setAutoReconnect(true);
        jdab.setStatus(OnlineStatus.ONLINE);
        jdab.setGame(Game.watching("Based on ArchT4"));
        try{
            jdab.buildBlocking();
        } catch (Exception err){
            System.out.println("An error occured during the connection between the API and the bot.");
        }
    }

}
