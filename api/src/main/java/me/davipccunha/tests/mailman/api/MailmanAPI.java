package me.davipccunha.tests.mailman.api;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface MailmanAPI {
    void addItem(String name, ItemStack item);

    void addItemToAll(ItemStack item);

    void removeItem(String name, ItemStack item);

    Collection<ItemStack> getItems(String name);

    void clear(String name);

    boolean isMailboxEmpty(String name);
}
