package me.davipccunha.tests.mailman.factory.view;

import me.davipccunha.tests.mailman.model.Mailbox;
import me.davipccunha.utils.inventory.InteractiveInventory;
import me.davipccunha.utils.inventory.InventoryUtil;
import me.davipccunha.utils.item.ItemName;
import me.davipccunha.utils.item.ItemSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MailboxGUI {
    public static Inventory createMailboxGUI(Mailbox mailbox, boolean adminView) {
        final Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Correio");

        Collection<ItemStack> items = mailbox.getItems();

        if (items == null || items.isEmpty()) return createEmptyMailbox();

        final List<ItemStack> firstPage = InventoryUtil.separateIntoPages(items, 36).get(0);

        final List<String> mailboxItemLore = adminView ?
                List.of("§7 Clique para remover este item") :
                List.of("§7 Clique para coletar este item");

        for (ItemStack item : firstPage) {
            final Map<String, String> mailboxItemNBTTags = adminView ?
                    Map.of(
                    "action", "remove_item",
                    "item", ItemSerializer.serialize(item),
                    "owner", mailbox.getOwner()
                    ) :
                    Map.of(
                    "action", "collect_item",
                    "item", ItemSerializer.serialize(item)
            );

            final String mailboxName = item.getItemMeta().getDisplayName() == null ?
                    "§e" + ItemName.valueOf(item).toString()
                    : "§e" + item.getItemMeta().getDisplayName();

            final ItemStack mailboxItem = InteractiveInventory.createActionItem(item, mailboxItemNBTTags, mailboxName, mailboxItemLore);

            inventory.addItem(mailboxItem);
        }

        if (items.size() > 36) {
            final ItemStack arrow = new ItemStack(Material.ARROW);
            final String nextPageName = "§r§ePróxima página";
            final Map<String, String> nextPageNBTTags = Map.of(
                    "action", "next_page",
                    "page", "1"
            );
            final List<String> nexPageLore = List.of(
                    "§7 Clique para ir para a próxima página"
            );

            final ItemStack nextPage = InteractiveInventory.createActionItem(arrow, nextPageNBTTags, nextPageName, nexPageLore);

            inventory.setItem(51, nextPage);
        }

        return inventory;
    }

    private static Inventory createEmptyMailbox() {
        final Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Correio");

        final ItemStack empty = new ItemStack(Material.BARRIER);
        final ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName("§r§cVazio");
        emptyMeta.setLore(List.of("§7 Não há itens em seu correio"));

        empty.setItemMeta(emptyMeta);

        inventory.setItem(22, empty);

        return inventory;
    }
}
