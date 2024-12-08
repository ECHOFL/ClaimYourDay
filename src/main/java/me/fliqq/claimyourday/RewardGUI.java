package me.fliqq.claimyourday;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RewardGUI {
    private final RewardManager rewardManager;
    private final PlayerManager playerManager;

    public RewardGUI(RewardManager rewardManager, PlayerManager playerManager) {
        this.rewardManager = rewardManager;
        this.playerManager = playerManager;
    }

    @SuppressWarnings("deprecation")
    public void openGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Daily Rewards");
        PlayerData playerData = playerManager.getPlayerData(player.getUniqueId());
        int lastClaimedReward = playerData.getLastRewardId();
        Date lastClaimTime = playerData.getLastClaimTime();
        Date now = new Date();
        long timeDifference = now.getTime() - lastClaimTime.getTime();
        boolean canClaim = timeDifference >= 24 * 60 * 60 * 1000;

        for (int i = 1; i <= 20; i++) {
            Reward reward = rewardManager.getReward(i);
            ItemStack displayItem;
            
            if (i <= lastClaimedReward) {
                displayItem = getDisplayItem(reward, ChatColor.BLUE + "Claimed");
            } else if (i == lastClaimedReward + 1) {
                if (canClaim) {
                    displayItem = getDisplayItem(reward, ChatColor.GREEN + "Click to Claim!");
                } else {
                    long timeLeft = 24 * 60 * 60 * 1000 - timeDifference;
                    String timeLeftString = formatTimeLeft(timeLeft);
                    displayItem = getDisplayItem(reward, ChatColor.RED + "Available in: " + timeLeftString);
                }
            } else {
                displayItem = new ItemStack(Material.RED_WOOL);
                ItemMeta meta = displayItem.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + "Future Reward");
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + "Claim more rewards to unlock!");
                    meta.setLore(lore);
                    displayItem.setItemMeta(meta);
                }
            }
            
            gui.setItem(i - 1, displayItem);
        }

        player.openInventory(gui);
    }

    @SuppressWarnings("deprecation")
    private ItemStack getDisplayItem(Reward reward, String status) {
        ItemStack displayItem;
        if (reward.isItem()) {
            displayItem = reward.getItem().clone();
        } else {
            displayItem = new ItemStack(Material.PAPER);
            ItemMeta meta = displayItem.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.YELLOW + "Command Reward");
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + reward.getCommand());
                meta.setLore(lore);
                displayItem.setItemMeta(meta);
            }
        }

        ItemMeta meta = displayItem.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore();
            if (lore == null) lore = new ArrayList<>();
            lore.add(status);
            meta.setLore(lore);
            displayItem.setItemMeta(meta);
        }

        return displayItem;
    }

    private String formatTimeLeft(long milliseconds) {
        long hours = milliseconds / (60 * 60 * 1000);
        long minutes = (milliseconds % (60 * 60 * 1000)) / (60 * 1000);
        return String.format("%d hours and %d minutes", hours, minutes);
    }
}
