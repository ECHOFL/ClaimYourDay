package me.fliqq.claimyourday;

import org.bukkit.plugin.java.JavaPlugin;

public class ClaimYourDay extends JavaPlugin
{
    private PlayerManager playerManager;
    private RewardManager rewardManager;
    private RewardGUI rewardGUI;
    @Override
    public void onEnable(){
        playerManager=new PlayerManager(this);
        rewardManager= new RewardManager(this);
        rewardGUI=new RewardGUI(rewardManager, playerManager);
        
        getServer().getPluginManager().registerEvents(new GUIListener(rewardGUI), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(playerManager), this);
        getCommand("daily").setExecutor(new RewardsCommand(rewardGUI));
        getCommand("claimreward").setExecutor(new ClaimRewardCommand(rewardManager, playerManager));

        messages();
    }
        

    @Override
    public void onDisable() {
        playerManager.saveAllPlayers();
        getLogger().info("ClaimYourDay has been disabled!");
    }

    private void messages() {
        getLogger().info("***********");
        getLogger().info("ClaimYourDay 1.0 enabled");
        getLogger().info("Plugin by Fliqqq");
        getLogger().info("***********");
    }
}
