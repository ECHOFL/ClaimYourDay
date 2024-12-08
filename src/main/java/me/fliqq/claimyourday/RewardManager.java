package me.fliqq.claimyourday;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class RewardManager {
    private final ClaimYourDay plugin;
    private final Map<Integer, Reward> rewards;
    private File file;
    private FileConfiguration config;

    public RewardManager(ClaimYourDay plugin){
        this.plugin=plugin;
        this.file=new File(plugin.getDataFolder(), "rewards.yml");
        this.rewards = new HashMap<>();
        loadRewards();
    }
        
    private void loadRewards() {
        if(!file.exists())
            plugin.saveResource("rewards.yml", false);
        config = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection rewardsSection = config.getConfigurationSection("rewards");
        if(rewardsSection == null)
            throw new IllegalStateException("No rewards defined in rewards.yml");
        
        for(String key : rewardsSection.getKeys(false)){
            int day = Integer.parseInt(key);
            ConfigurationSection rewardsConfig = rewardsSection.getConfigurationSection(key);

            if(rewardsConfig == null) continue;

            String type = rewardsConfig.getString("type", null);
            if(type == null || (!type.equals("item") && !type.equals("command"))){
                throw new IllegalStateException("Invalid reward type: " + type);
            }
            
            if("item".equals(type)){
                ItemStack item = createItemFromConfig(rewardsConfig);
                rewards.put(day, new Reward(item, day));
            } else if ("command".equals(type)){
                String command = rewardsConfig.getString("command");
                rewards.put(day, new Reward(command, day));
            }
            
        }

    }

    @SuppressWarnings("deprecation")
    private ItemStack createItemFromConfig(ConfigurationSection section){
        Material material = Material.valueOf(section.getString("material"));
        int amount = section.getInt("amount",1);
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if(meta!=null){
            String name = section.getString("name");
            if(name != null){
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }
            List<String> lore = section.getStringList("lore");
            if(!lore.isEmpty()){
                meta.setLore(lore.stream().map(line-> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
            }
            ConfigurationSection enchants = section.getConfigurationSection("enchantments");
            if(enchants != null){
                for(String enchantName: enchants.getKeys(false)){
                    Enchantment enchantment = Enchantment.getByName(enchantName);
                    int lvl = enchants.getInt(enchantName);
                    if(enchantment != null){
                        meta.addEnchant(enchantment, lvl, true);
                    }
                }
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public Reward getReward(int day){
        return rewards.get(day);
    }

    public Map<Integer,Reward> getRewards(){
        return rewards;
    }
}
