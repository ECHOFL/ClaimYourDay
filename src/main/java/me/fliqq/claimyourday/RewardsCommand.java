package me.fliqq.claimyourday;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RewardsCommand implements CommandExecutor {
    private final RewardGUI rewardGUI;

    public RewardsCommand(RewardGUI rewardGUI) {
        this.rewardGUI=rewardGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        rewardGUI.openGUI(player);
        return true;
    }
}

