package me.wolfyscript.utilities.api.inventory.custom_items.meta;


import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class CustomItemTagMeta extends Meta {

    public static final NamespacedKey namespacedKey = new NamespacedKey("wolfyutilities", "custom_item");

    public CustomItemTagMeta() {
        setOption(MetaSettings.Option.IGNORE);
        setAvailableOptions(MetaSettings.Option.IGNORE, MetaSettings.Option.EXACT);
    }

    @Override
    public boolean check(ItemBuilder itemOther, ItemBuilder item) {
        if (option.equals(MetaSettings.Option.IGNORE)) {
            return true;
        }
        ItemMeta meta1 = item.getItemMeta();
        ItemMeta meta2 = itemOther.getItemMeta();
        if (hasKey(meta1)) {
            return hasKey(meta2) && Objects.equals(getKey(meta1), getKey(meta2));
        }
        return !hasKey(meta2);
    }

    private boolean hasKey(ItemMeta meta) {
        return meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING);
    }

    private me.wolfyscript.utilities.util.NamespacedKey getKey(ItemMeta meta) {
        return me.wolfyscript.utilities.util.NamespacedKey.of(meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING));
    }
}
