package me.wolfyscript.utilities.api.inventory.custom_items.meta;

import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;

public class CustomDamageMeta extends Meta {

    public CustomDamageMeta() {
        setOption(MetaSettings.Option.EXACT);
        setAvailableOptions(MetaSettings.Option.EXACT, MetaSettings.Option.IGNORE, MetaSettings.Option.HIGHER, MetaSettings.Option.LOWER);
    }

    @Override
    public boolean check(ItemBuilder itemOther, ItemBuilder item) {
        if (option.equals(MetaSettings.Option.IGNORE)) {
            return true;
        }
        boolean meta0 = item.hasCustomDurability();
        boolean meta1 = itemOther.hasCustomDurability();
        if (meta0 && meta1) {
            switch (option) {
                case EXACT:
                    return itemOther.getCustomDamage() == item.getCustomDamage();
                case LOWER:
                    return itemOther.getCustomDamage() < item.getCustomDamage();
                case HIGHER:
                    return itemOther.getCustomDamage() > item.getCustomDamage();
                default:
                    return false;
            }
        } else return !meta0 && !meta1;
    }
}
