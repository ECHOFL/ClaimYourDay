package me.fliqq.claimyourday;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Reward {
    private int day;
    private ItemStack item;
    private String command;

    public Reward(ItemStack item, int day
    ){
        this.day=day;
        this.item=item;
    }
    public Reward(String command, int day){
        this.command=command;
        this.day=day;
    }

    public boolean isItem(){
        return item != null;
    }
    public ItemStack getItem(){
        return item;
    }
    
    public String getCommand(){
        return command;
    }
    
    public void giveReward(Player player){
        if(isItem()){
            player.getInventory().addItem(item);
        }else if
        (command != null){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
        }
    }

    public int getDay(){
        return day;
    }
    
}
