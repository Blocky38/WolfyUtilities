package me.wolfyscript.utilities.api.inventory.custom_items.meta;


import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class PotionMeta extends Meta {

    public PotionMeta() {
        setOption(MetaSettings.Option.EXACT);
        setAvailableOptions(MetaSettings.Option.EXACT, MetaSettings.Option.IGNORE);
    }

    @Override
    public boolean check(ItemBuilder itemOther, ItemBuilder item) {
        if (option.equals(MetaSettings.Option.IGNORE)) {
            return true;
        }
        ItemMeta meta1 = itemOther.getItemMeta();
        ItemMeta meta2 = item.getItemMeta();
        if (meta2 instanceof org.bukkit.inventory.meta.PotionMeta) {
            if (meta1 instanceof org.bukkit.inventory.meta.PotionMeta) {
                org.bukkit.inventory.meta.PotionMeta metaThat = (org.bukkit.inventory.meta.PotionMeta) itemOther.getItemMeta();
                org.bukkit.inventory.meta.PotionMeta metaThis = (org.bukkit.inventory.meta.PotionMeta) item.getItemMeta();
                if (metaThis.getBasePotionData().getType().equals(metaThat.getBasePotionData().getType())) {
                    if (metaThis.hasCustomEffects()) {
                        if (!metaThat.hasCustomEffects() || !metaThis.getCustomEffects().equals(metaThat.getCustomEffects())) {
                            return false;
                        }
                    } else if (metaThat.hasCustomEffects()) {
                        return false;
                    }
                    if (metaThis.hasColor()) {
                        return metaThat.hasColor() && Objects.equals(metaThis.getColor(), metaThat.getColor());
                    } else return !metaThat.hasColor();
                }
            }
            return false;
        }
        return !(meta1 instanceof org.bukkit.inventory.meta.PotionMeta);
    }
}
