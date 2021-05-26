package me.wolfyscript.utilities.api.inventory.custom_items.meta;


import com.google.common.collect.Multimap;
import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.ItemMeta;

public class AttributesModifiersMeta extends Meta {

    public AttributesModifiersMeta() {
        setOption(MetaSettings.Option.EXACT);
        setAvailableOptions(MetaSettings.Option.EXACT, MetaSettings.Option.IGNORE);
    }

    private static boolean compareModifiers(Multimap<Attribute, AttributeModifier> first, Multimap<Attribute, AttributeModifier> second) {
        if (first != null && second != null) {
            return first.entries().stream().allMatch(entry -> second.containsEntry(entry.getKey(), entry.getValue())) && second.entries().stream().allMatch(entry -> first.containsEntry(entry.getKey(), entry.getValue()));
        } else {
            return false;
        }
    }

    @Override
    public boolean check(ItemBuilder itemOther, ItemBuilder item) {
        ItemMeta metaOther = itemOther.getItemMeta();
        ItemMeta meta = item.getItemMeta();
        if (option.equals(MetaSettings.Option.EXACT)) {
            if (meta.hasAttributeModifiers()) {
                return metaOther.hasAttributeModifiers() && compareModifiers(meta.getAttributeModifiers(), metaOther.getAttributeModifiers());
            }
            return !metaOther.hasAttributeModifiers();
        }
        return true;
    }
}
