package me.wolfyscript.utilities.api.inventory.custom_items.meta.legacy;


import me.wolfyscript.utilities.api.inventory.custom_items.meta.LegacyMetaSettings;
import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class DamageMeta extends Meta {

    public DamageMeta() {
        setOption(LegacyMetaSettings.Option.EXACT);
        setAvailableOptions(LegacyMetaSettings.Option.EXACT, LegacyMetaSettings.Option.IGNORE, LegacyMetaSettings.Option.HIGHER, LegacyMetaSettings.Option.LOWER);
    }

    @Override
    public boolean check(ItemBuilder itemOther, ItemBuilder item) {
        ItemMeta metaOther = itemOther.getItemMeta();
        ItemMeta meta = item.getItemMeta();
        switch (option) {
            case EXACT:
                return ((Damageable)metaOther).getDamage() == ((Damageable)meta).getDamage();
            case IGNORE:
                ((Damageable)metaOther).setDamage(0);
                ((Damageable)meta).setDamage(0);
                itemOther.setItemMeta(metaOther);
                item.setItemMeta(meta);
                return true;
            case LOWER:
                return ((Damageable)metaOther).getDamage() < ((Damageable)meta).getDamage();
            case HIGHER:
                return ((Damageable)metaOther).getDamage() > ((Damageable)meta).getDamage();
        }
        return false;
    }
}
