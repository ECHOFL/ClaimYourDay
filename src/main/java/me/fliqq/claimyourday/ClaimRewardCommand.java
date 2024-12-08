package me.fliqq.claimyourday;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

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
        UUID uuid = player.getUniqueId();
        PlayerData playerData = playerManager.getPlayerData(uuid);

        if (playerData == null) {
            player.sendMessage("Error: Your player data could not be found.");
            return true;
        }

        Date now = new Date();
        Date lastClaimTime = playerData.getLastClaimTime();
        long timeDifference = now.getTime() - lastClaimTime.getTime();

        if (timeDifference < 24 * 60 * 60 * 1000) { // 24 hours in milliseconds
            long timeLeft = 24 * 60 * 60 * 1000 - timeDifference;
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
        playerManager.updatePlayerData(uuid, nextRewardId);
        playerManager.savePlayer(player); // Save immediately after updating
        player.sendMessage("You've claimed your daily reward!");

        return true;
    }

    private String formatTimeLeft(long milliseconds) {
        long hours = milliseconds / (60 * 60 * 1000);
        long minutes = (milliseconds % (60 * 60 * 1000)) / (60 * 1000);
        return String.format("%d hours and %d minutes", hours, minutes);
    }
}
