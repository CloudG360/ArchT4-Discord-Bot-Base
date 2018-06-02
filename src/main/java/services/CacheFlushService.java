package main.java.services;

import main.java.Main;
import net.dv8tion.jda.core.entities.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CacheFlushService extends Thread{

    CacheService cacheService;

    public CacheFlushService(CacheService service){
        cacheService = service;
    }



    @Override
    public void run(){

        while(Main.getResources().killInitiated == 0){
            try {
                TimeUnit.MINUTES.sleep(Long.parseLong(Main.getResources().botAdministratorConfig.get("cache-flush_interval").toString()));
                cacheService.cacheTree.clear();
                cacheService.setupCaches();
                Main.getResources().coreService.SendDebugToHome("Cleared Cache", "Cleared Cache in a cleanup call.", "");
            } catch (Exception err){
            }
        }
    }


}
