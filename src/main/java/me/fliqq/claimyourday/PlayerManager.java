package me.fliqq.claimyourday;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerManager {
    private final Map<UUID, PlayerData> playerData;
    private File file;
    private FileConfiguration config;
    private final ClaimYourDay plugin;

    public PlayerManager(ClaimYourDay plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "players.yml");
        this.playerData = new HashMap<>();
        loadAllPlayers();
    }

    private void loadAllPlayers() {
        if (!file.exists()) {
            plugin.saveResource("players.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        
        for (String uuidString : config.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            ConfigurationSection playerSection = config.getConfigurationSection(uuidString);
            if (playerSection != null) {
                int lastRewardId = playerSection.getInt("last-reward-id", 0);
                long lastClaimTime = playerSection.getLong("last-claim-time", 0);
                playerData.put(uuid, new PlayerData(lastRewardId, new Date(lastClaimTime)));
            }
        }
    }

    public void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerData.containsKey(uuid)) {
            ConfigurationSection playerSection = config.getConfigurationSection(uuid.toString());
            if (playerSection != null) {
                int lastRewardId = playerSection.getInt("last-reward-id", 0);
                long lastClaimTime = playerSection.getLong("last-claim-time", 0);
                playerData.put(uuid, new PlayerData(lastRewardId, new Date(lastClaimTime)));
            } else {
                playerData.put(uuid, new PlayerData(0, new Date(0)));
            }
        }
    }

    public void savePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerData data = playerData.get(uuid);
        if (data != null) {
            config.set(uuid.toString() + ".last-reward-id", data.getLastRewardId());
            config.set(uuid.toString() + ".last-claim-time", data.getLastClaimTime().getTime());
            try {
                config.save(file);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not save player data for " + player.getName(), e);
            }
        }
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    public void updatePlayerData(UUID uuid, int newRewardId) {
        PlayerData data = playerData.get(uuid);
        if (data != null) {
            data.setLastRewardId(newRewardId);
            data.setLastClaimTime(new Date());
        }
    }

    public void saveAllPlayers() {
        for (Map.Entry<UUID, PlayerData> entry : playerData.entrySet()) {
            UUID uuid = entry.getKey();
            PlayerData data = entry.getValue();
            config.set(uuid.toString() + ".last-reward-id", data.getLastRewardId());
            config.set(uuid.toString() + ".last-claim-time", data.getLastClaimTime().getTime());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save player data", e);
        }
    }
}

