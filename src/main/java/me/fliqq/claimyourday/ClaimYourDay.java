package me.fliqq.claimyourday;

import org.bukkit.plugin.java.JavaPlugin;

public class ClaimYourDay extends JavaPlugin
{
    @Override
    public void onEnable(){
        messages();
    }
        
    private void messages() {
        getLogger().info("***********");
        getLogger().info("ClaimYourDay 1.0 enabled");
        getLogger().info("Plugin by Fliqqq");
        getLogger().info("***********");
    }
}
