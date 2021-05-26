package me.wolfyscript.utilities.api.inventory.custom_items.meta;


import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;

public class UnbreakableMeta extends Meta {

    public UnbreakableMeta() {
        setOption(MetaSettings.Option.EXACT);
        setAvailableOptions(MetaSettings.Option.EXACT, MetaSettings.Option.IGNORE);
    }

    @Override
    public boolean check(ItemBuilder itemOther, ItemBuilder item) {
        if (option.equals(MetaSettings.Option.IGNORE)) {
            return true;
        }
        return item.getItemMeta().isUnbreakable() == itemOther.getItemMeta().isUnbreakable();
    }
}
