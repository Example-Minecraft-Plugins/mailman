package me.davipccunha.tests.mailman.provider;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.mailman.api.MailmanAPI;
import me.davipccunha.tests.mailman.model.Mailbox;
import me.davipccunha.utils.cache.RedisCache;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;

@RequiredArgsConstructor
public class MailmanProvider implements MailmanAPI {
    private final RedisCache<Mailbox> cache;

    @Override
    public void addItem(String name, ItemStack item) {
        Mailbox mailbox = this.cache.get(name.toLowerCase());

        if (mailbox == null)
            mailbox = new Mailbox(name);

        mailbox.addItem(item);
        cache.add(name, mailbox);
    }

    @Override
    public void addItemToAll(ItemStack item) {
        for (Mailbox mailbox : cache.getValues()) {
            mailbox.addItem(item);
            cache.add(mailbox.getOwner().toLowerCase(), mailbox);
        }
    }

    @Override
    public void removeItem(String name, ItemStack item) {
        final Mailbox mailbox = this.cache.get(name.toLowerCase());

        if (mailbox == null) return;

        mailbox.removeItem(item);
        cache.add(name.toLowerCase(), mailbox);
    }

    @Override
    public @Nullable Collection<ItemStack> getItems(String name) {
        final Mailbox mailbox = this.cache.get(name.toLowerCase());

        if (mailbox == null) return null;

        return mailbox.getItems();
    }

    @Override
    public void clear(String name) {
        final Mailbox mailbox = this.cache.get(name.toLowerCase());

        if (mailbox == null) return;

        mailbox.clearItems();
        cache.add(name.toLowerCase(), mailbox);
    }

    @Override
    public boolean isMailboxEmpty(String name) {
        Mailbox mailbox = this.cache.get(name.toLowerCase());

        return mailbox == null || mailbox.isEmpty();
    }
}
