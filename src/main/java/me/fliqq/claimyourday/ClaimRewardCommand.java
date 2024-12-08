package me.fliqq.claimyourday;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class ClaimRewardCommand implements CommandExecutor {

    private final RewardManager rewardManager;
    private final PlayerManager playerManager;

    public ClaimRewardCommand(RewardManager rewardManager, PlayerManager playerManager) {
        this.rewardManager = rewardManager;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can claim rewards.");
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = playerManager.getPlayerData(player.getUniqueId());

        if (playerData == null) {
            player.sendMessage("Error: Your player data could not be found.");
            return true;
        }

        Date lastClaimTime = playerData.getLastClaimTime();
        Date now = new Date();

        // Check if 24 hours have passed since last claim
        if (lastClaimTime != null && (now.getTime() - lastClaimTime.getTime() < 24 * 60 * 60 * 1000)) {
            long timeLeft = 24 * 60 * 60 * 1000 - (now.getTime() - lastClaimTime.getTime());
            player.sendMessage("You can claim your next reward in " + formatTimeLeft(timeLeft));
            return true;
        }

        int nextRewardId = playerData.getLastRewardId() + 1;
        Reward reward = rewardManager.getReward(nextRewardId);

        if (reward == null) {
            player.sendMessage("No more rewards available.");
            return true;
        }

        reward.giveReward(player);
        playerManager.updatePlayerData(player.getUniqueId(), nextRewardId);
        player.sendMessage("You've claimed your daily reward!");

        return true;
    }

    private String formatTimeLeft(long milliseconds) {
        long hours = milliseconds / (60 * 60 * 1000);
        long minutes = (milliseconds % (60 * 60 * 1000)) / (60 * 1000);
        return String.format("%d hours and %d minutes", hours, minutes);
    }
}
