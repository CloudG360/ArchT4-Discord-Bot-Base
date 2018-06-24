package main.java.services;

import main.java.Commands.CommandBase;
import main.java.Main;

public class JanitorService extends Thread {

    public JanitorService(){

    }

    /**
     * @apiNote The base of the JanitorService. It runs
     * the shutdown cleanups.
     */
    @Override
    public void run() {
        Main.getResources().coreService.SendInfoToHome("Clearing Cache", "Disposed of Cache (Shutdown)", "JanitorService#"+this.getId());
        Main.getResources().cacheService.cacheTree.clear();
        Main.getResources().cacheService.interrupt();
        Main.getResources().services.remove(Main.getResources().cacheService);

        Main.getResources().coreService.SendInfoToHome("Killing Services", "Disposing of services.", "JanitorService#"+this.getId());
        for(Thread service: Main.getResources().services){
            if(service != Main.getResources().commandKillParent) {
                Main.getResources().coreService.SendInfoToHome("Service Closed", "Ended "+service.getName()+"#"+service.getId(), "JanitorService#"+this.getId());
                service.interrupt();
                Main.getResources().services.remove(service);
            }
        }


    }
}
