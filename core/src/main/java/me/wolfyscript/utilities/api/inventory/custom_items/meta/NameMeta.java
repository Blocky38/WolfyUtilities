package me.wolfyscript.utilities.api.inventory.custom_items.meta;

import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;
import org.bukkit.inventory.meta.ItemMeta;

public class NameMeta extends Meta {

    public NameMeta() {
        setOption(MetaSettings.Option.EXACT);
        setAvailableOptions(MetaSettings.Option.EXACT, MetaSettings.Option.IGNORE);
    }

    @Override
    public boolean check(ItemBuilder itemOther, ItemBuilder item) {
        if (option.equals(MetaSettings.Option.IGNORE)) {
            return true;
        }
        ItemMeta metaOther = itemOther.getItemMeta();
        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()) {
            return metaOther.hasDisplayName() && meta.getDisplayName().equals(metaOther.getDisplayName());
        } else return !metaOther.hasDisplayName();
    }
}
