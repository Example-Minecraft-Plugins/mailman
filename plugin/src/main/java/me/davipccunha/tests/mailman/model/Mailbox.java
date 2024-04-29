package me.davipccunha.tests.mailman.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.davipccunha.utils.item.ItemSerializer;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class Mailbox {
    private final String owner;
    private String serializedItems;

    public Mailbox addItem(ItemStack item) {
        if (this.serializedItems == null) {
            this.serializedItems = ItemSerializer.serialize(item);
            return this;
        } else {
            final ItemStack[] items = ItemSerializer.deserialize(this.serializedItems);
            final List<ItemStack> itemsAsList = Arrays.stream(items).collect(Collectors.toList());

            itemsAsList.add(item);
            this.serializedItems = ItemSerializer.serialize(itemsAsList.toArray(new ItemStack[0]));

            return this;
        }
    }

    public Mailbox removeItem(ItemStack item) {
        if (this.serializedItems == null) return this;

        final ItemStack[] items = ItemSerializer.deserialize(this.serializedItems);
        final List<ItemStack> itemsAsList = Arrays.stream(items).collect(Collectors.toList());

        itemsAsList.remove(item);

        this.serializedItems = ItemSerializer.serialize(itemsAsList.toArray(new ItemStack[0]));

        return this;
    }

    public @Nullable Collection<ItemStack> getItems() {
        if (this.serializedItems == null) return null;

        ItemStack[] itemsArray = ItemSerializer.deserialize(this.serializedItems);

        return Arrays.stream(itemsArray).collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return this.serializedItems == null;
    }

    public void clearItems() {
        this.serializedItems = null;
    }
}
