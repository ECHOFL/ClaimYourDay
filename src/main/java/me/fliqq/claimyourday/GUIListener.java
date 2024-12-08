package me.fliqq.claimyourday;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {
    private final RewardGUI rewardGUI;

    public GUIListener(RewardGUI rewardGUI) {
        this.rewardGUI=rewardGUI;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.GOLD + "Daily Rewards")) return;
        
        event.setCancelled(true);
        
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        
        if (slot >= 0 && slot < 20) {
            // Execute the claim reward command
            player.performCommand("claimreward");
            // Refresh the GUI
            rewardGUI.openGUI(player);
        }
    }
}
