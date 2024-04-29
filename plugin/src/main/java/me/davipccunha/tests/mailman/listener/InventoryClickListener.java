package me.davipccunha.tests.mailman.listener;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.mailman.MailmanPlugin;
import me.davipccunha.tests.mailman.factory.view.MailboxGUI;
import me.davipccunha.tests.mailman.model.Mailbox;
import me.davipccunha.utils.cache.RedisCache;
import me.davipccunha.utils.inventory.InventoryUtil;
import me.davipccunha.utils.item.ItemSerializer;
import me.davipccunha.utils.item.NBTHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class InventoryClickListener implements Listener {
    private final MailmanPlugin plugin;

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory == null) return;
        if (event.getCurrentItem() == null) return;

        String inventoryName = inventory.getName();
        if (!inventoryName.equals("Correio")) return;

        event.setCancelled(true);

        if (!event.getCurrentItem().hasItemMeta()) return;
        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

        final Player player = (Player) event.getWhoClicked();

        if (player == null) return;

        ItemStack clickedItem = event.getCurrentItem();
        String action = NBTHandler.getNBT(clickedItem, "action");

        if (action == null) return;

        final String serializedOriginalItem = NBTHandler.getNBT(clickedItem, "item");

        switch (action) {
            case "collect_item":
                if (serializedOriginalItem == null) return;
                this.collectItem(player, ItemSerializer.deserialize(serializedOriginalItem)[0]);
                final Mailbox updatedMailbox = this.plugin.getMailboxCache().get(player.getName());
                player.openInventory(MailboxGUI.createMailboxGUI(updatedMailbox, false));
                break;

            case "remove_item":
                if (serializedOriginalItem == null) return;
                final String owner = NBTHandler.getNBT(clickedItem, "owner");
                if (owner == null) return;

                final ItemStack item = ItemSerializer.deserialize(serializedOriginalItem)[0];
                final Mailbox mailbox = this.plugin.getMailboxCache().get(owner);
                final Mailbox updatedMailbox_ = mailbox.removeItem(item);

                this.plugin.getMailboxCache().add(updatedMailbox_.getOwner(), updatedMailbox_);

                player.sendMessage(String.format("§aItem removido do correio de §f%s §acom sucesso.", mailbox.getOwner()));
                player.openInventory(MailboxGUI.createMailboxGUI(updatedMailbox_, true));

            case "next_page":
                break;
        }
    }

    private void collectItem(Player player, ItemStack item) {
        final RedisCache<Mailbox> cache = this.plugin.getMailboxCache();

        final int missingAmount = InventoryUtil.getMissingAmount(player.getInventory(), item);
        if (missingAmount < item.getAmount()) {
            player.sendMessage("§cVocê não tem espaço suficiente no inventário para coletar este item.");
            return;
        }

        Mailbox mailbox = cache.get(player.getName());
        if (mailbox == null) return;

        mailbox.removeItem(item);
        cache.add(player.getName(), mailbox);

        player.getInventory().addItem(item);
        player.sendMessage("§aVocê coletou um item do seu correio com sucesso.");
    }
}
