package me.wolfyscript.utilities.api.inventory.custom_items.meta;


import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

public class RepairCostMeta extends Meta {

    public RepairCostMeta() {
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
        if (meta instanceof Repairable) {
            if (metaOther instanceof Repairable) {
                switch (option) {
                    case EXACT:
                        return ((Repairable) metaOther).getRepairCost() == ((Repairable) meta).getRepairCost();
                    case LOWER:
                        return ((Repairable) metaOther).getRepairCost() < ((Repairable) meta).getRepairCost();
                    case HIGHER:
                        return ((Repairable) metaOther).getRepairCost() > ((Repairable) meta).getRepairCost();
                    default:
                        return false;
                }
            }
            return false;
        }
        return !(metaOther instanceof Repairable);
    }
}
