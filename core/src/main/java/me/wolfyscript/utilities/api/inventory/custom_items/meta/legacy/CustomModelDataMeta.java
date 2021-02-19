package me.wolfyscript.utilities.api.inventory.custom_items.meta.legacy;


import me.wolfyscript.utilities.api.inventory.custom_items.meta.LegacyMetaSettings;
import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomModelDataMeta extends Meta {

    public CustomModelDataMeta() {
        setOption(LegacyMetaSettings.Option.EXACT);
        setAvailableOptions(LegacyMetaSettings.Option.EXACT, LegacyMetaSettings.Option.IGNORE, LegacyMetaSettings.Option.HIGHER, LegacyMetaSettings.Option.LOWER);
    }

    @Override
    public boolean check(ItemBuilder itemOther, ItemBuilder item) {
        ItemMeta meta1 = itemOther.getItemMeta();
        ItemMeta meta2 = item.getItemMeta();
        switch (option) {
            case IGNORE:
                meta1.setCustomModelData(0);
                meta2.setCustomModelData(0);
                itemOther.setItemMeta(meta1);
                item.setItemMeta(meta2);
                return true;
            case LOWER:
                return meta1.getCustomModelData() < meta2.getCustomModelData();
            case HIGHER:
                return meta1.getCustomModelData() > meta2.getCustomModelData();
        }
        return true;
    }
}
