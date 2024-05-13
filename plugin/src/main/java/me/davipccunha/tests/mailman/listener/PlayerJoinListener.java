package me.davipccunha.tests.mailman.listener;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.mailman.MailmanPlugin;
import me.davipccunha.tests.mailman.model.Mailbox;
import me.davipccunha.utils.cache.RedisCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {
    private final MailmanPlugin plugin;

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(PlayerJoinEvent event) {
        final RedisCache<Mailbox> mailboxCache = plugin.getMailboxCache();
        final String playerName = event.getPlayer().getName();

        if (mailboxCache.get(playerName.toLowerCase()) == null)
            mailboxCache.add(playerName.toLowerCase(), new Mailbox(playerName));
    }
}
