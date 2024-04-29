package me.davipccunha.tests.mailman;

import lombok.Getter;
import me.davipccunha.tests.mailman.api.MailmanAPI;
import me.davipccunha.tests.mailman.command.CorreioCommand;
import me.davipccunha.tests.mailman.listener.InventoryClickListener;
import me.davipccunha.tests.mailman.listener.PlayerJoinListener;
import me.davipccunha.tests.mailman.model.Mailbox;
import me.davipccunha.tests.mailman.provider.MailmanProvider;
import me.davipccunha.utils.cache.RedisCache;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class MailmanPlugin extends JavaPlugin {
    private RedisCache<Mailbox> mailboxCache;

    @Override
    public void onEnable() {
        this.init();
        getLogger().info("Mailman plugin loaded!");
    }

    public void onDisable() {
        getLogger().info("Mailman plugin unloaded!");
    }

    private void init() {
        saveDefaultConfig();

        registerListeners(
                new PlayerJoinListener(this),
                new InventoryClickListener(this)
        );

        this.registerCommands();
        this.loadCaches();

        Bukkit.getServicesManager().register(MailmanAPI.class, new MailmanProvider(mailboxCache), this, ServicePriority.Normal);
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();

        for (Listener listener : listeners) pluginManager.registerEvents(listener, this);
    }

    private void registerCommands() {
        this.getCommand("correio").setExecutor(new CorreioCommand(this));
    }

    private void loadCaches() {
        this.mailboxCache = new RedisCache<>("mailman:mailbox", Mailbox.class);
    }
}