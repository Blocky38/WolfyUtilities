package me.wolfyscript.utilities.api.inventory.custom_items.references;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class MMOItemsRef extends APIReference {

    private final Type itemType;
    private final String itemName;

    public MMOItemsRef(Type itemType, String itemName) {
        this.itemType = itemType;
        this.itemName = itemName;
    }

    @Override
    public ItemStack getLinkedItem() {
        return MMOItems.plugin.getItem(itemType, itemName);
    }

    @Override
    public ItemStack getIdItem() {
        return getLinkedItem();
    }

    @Override
    public boolean isValidItem(ItemStack itemStack) {
        NBTItem nbtItem = NBTItem.get(itemStack);
        if (nbtItem.hasType()) {
            return Objects.equals(itemType, MMOItems.plugin.getTypes().get(nbtItem.getType())) && Objects.equals(itemName, nbtItem.getString("MMOITEMS_ITEM_ID"));
        }
        return false;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObjectFieldStart("mmoitems");
        gen.writeStringField("type", itemType.getId());
        gen.writeStringField("name", itemName);
        gen.writeEndObject();
    }

    public static class Parser extends PluginParser<MMOItemsRef> {

        public Parser() {
            super("MMOItems", "mmoitems");
        }

        @Override
        public @Nullable MMOItemsRef construct(ItemStack itemStack) {
            NBTItem nbtItem = NBTItem.get(itemStack);
            if (nbtItem.hasType()) {
                Type type = MMOItems.plugin.getTypes().get(nbtItem.getType());
                String itemId = nbtItem.getString("MMOITEMS_ITEM_ID");
                return new MMOItemsRef(type, itemId);
            }
            return null;
        }

        @Override
        public @Nullable MMOItemsRef parse(JsonNode element) {
            if (element.has("type") && element.has("name")) {
                String typeID = element.get("type").asText();
                if (MMOItems.plugin.getTypes().has(typeID)) {
                    return new MMOItemsRef(MMOItems.plugin.getTypes().get(typeID), element.get("name").asText());
                }
            }
            return null;
        }
    }
}
