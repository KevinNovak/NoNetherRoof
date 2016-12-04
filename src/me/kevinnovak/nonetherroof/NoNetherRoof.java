package me.kevinnovak.nonetherroof;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NoNetherRoof extends JavaPlugin implements Listener{
    // ======================
    // Enable
    // ======================
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        if (getConfig().getBoolean("metrics")) {
            try {
                MetricsLite metrics = new MetricsLite(this);
                metrics.start();
                Bukkit.getServer().getLogger().info("[NoNetherRoof] Metrics Enabled!");
            } catch (IOException e) {
                Bukkit.getServer().getLogger().info("[NoNetherRoof] Failed to Start Metrics.");
            }
        } else {
            Bukkit.getServer().getLogger().info("[NoNetherRoof] Metrics Disabled.");
        }
        Bukkit.getServer().getLogger().info("[NoNetherRoof] Plugin Enabled!");
    }
    
    // ======================
    // Disable
    // ======================
    public void onDisable() {
        Bukkit.getServer().getLogger().info("[NoNetherRoof] Plugin Disabled!");
    }
    
    // =========================
    // Top of Nether
    // =========================
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (player.getWorld().getEnvironment() == Environment.NETHER) {
            if (player.hasPermission("nonetherroof.bypass")) {
                return;
            }
            int top = getConfig().getInt("netherTopLayer");
            if (player.getLocation().getY() > top) {
                Location toSpawn = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX() + 0.5, top ,player.getLocation().getBlockZ() + 0.5);
                toSpawn.subtract(0, 1, 0).getBlock().setType(Material.AIR);
                toSpawn.subtract(0, 1, 0).getBlock().setType(Material.AIR);
                toSpawn.subtract(0, 1, 0).getBlock().setType(Material.NETHERRACK);
                player.teleport(toSpawn.add(0,1,0));
                player.sendMessage(convertedLang("netherTopMessage"));
            }
        }
    }
    
    // =========================
    // Convert String in Config
    // =========================
    String convertedLang(String toConvert) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(toConvert));
    }
}