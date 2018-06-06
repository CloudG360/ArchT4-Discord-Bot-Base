package main.java.services;

import com.carrotsearch.sizeof.RamUsageEstimator;
import com.mysql.cj.util.TimeUtil;
import main.java.ClassTypes.OfflineMessage;
import main.java.Main;
import net.dv8tion.jda.core.entities.Role;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RoleColourService extends Thread{

    public static String[] colours = "f9100c#ff8800#fffb1e#15ff00#00ffe5#2a00ff#d400ff".split(",");

    private Role colourRole;

    public RoleColourService(Role role){
        colours = "#f9100c,#ff8800,#fffb1e,#15ff00,#00ffe5,#2a00ff,#d400ff".split(",");
        colourRole = role;
    }

    @Override
    public void run(){
        while(Main.getResources().killInitiated == 0) {
            try{
                TimeUnit.SECONDS.sleep(2);
                colourRole.getManager().setColor(Color.decode(colours[new Random().nextInt(colours.length)])).complete();
            } catch (Exception err) {

            }

        }

    }


}
