package main.java.services;

import main.java.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestGameService extends BaseGameService{


    public TestGameService(String id, int minLobbyStart, int maxLobbySize, int timerSize){
        super(id, minLobbyStart, maxLobbySize, timerSize);
    }
}
