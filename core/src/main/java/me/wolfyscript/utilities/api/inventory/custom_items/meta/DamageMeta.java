package me.wolfyscript.utilities.api.inventory.custom_items.meta;


import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class DamageMeta extends Meta {

    public DamageMeta() {
        setOption(MetaSettings.Option.EXACT);
        setAvailableOptions(MetaSettings.Option.EXACT, MetaSettings.Option.IGNORE, MetaSettings.Option.HIGHER, MetaSettings.Option.LOWER);
    }

    @Override
    public boolean check(ItemBuilder itemOther, ItemBuilder item) {
        if (option.equals(MetaSettings.Option.IGNORE)) {
            return true;
        }
        ItemMeta metaOther = itemOther.getItemMeta();
        ItemMeta meta = item.getItemMeta();
        switch (option) {
            case EXACT:
                return ((Damageable) metaOther).getDamage() == ((Damageable) meta).getDamage();
            case LOWER:
                return ((Damageable) metaOther).getDamage() < ((Damageable) meta).getDamage();
            case HIGHER:
                return ((Damageable) metaOther).getDamage() > ((Damageable) meta).getDamage();
            default:
                return false;
        }
    }
}
