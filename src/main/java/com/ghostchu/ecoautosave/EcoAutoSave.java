package com.ghostchu.ecoautosave;

import com.willfp.eco.core.Eco;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class EcoAutoSave extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        initTask();
    }

    public void initTask() {
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getScheduler().runTaskTimer(this, () -> save(Bukkit.getConsoleSender()), 0, getConfig().getLong("interval") * 20);
    }

    public void save(CommandSender sender) {
        long start = System.currentTimeMillis();
        Eco.getHandler().getProfileHandler().saveAll();
        Eco.getHandler().getProfileHandler().save();
        sender.sendMessage("Eco save completed. (" + (System.currentTimeMillis() - start) + "ms)");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ecoautosave.admin"))
            return false;
        if (args.length == 0) {
            sender.sendMessage("/ecoautosave reload/save");
            return true;
        }
        if (args[0].equals("reload")) {
            reloadConfig();
            initTask();
            sender.sendMessage("Config reloaded!");
            return true;
        }
        if (args[0].equals("save")) {
            save(sender);
            return true;
        }
        return false;
    }
}
