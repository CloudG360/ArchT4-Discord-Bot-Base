package main.java.services.gameservices;

import main.java.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TestGameService extends BaseGameService {


    public TestGameService(String id, int minLobbyStart, int maxLobbySize, int timerSize){
        super(id, minLobbyStart, maxLobbySize, timerSize);

        desc = "Welcome to a TEST lobby! This is a non-permanent game designed to allow developers to test the play script.";
        dispName = "TEST_LOBBY";
        icon = "https://emojipedia-us.s3.amazonaws.com/thumbs/120/google/119/construction-worker_1f477.png";
    }

}
