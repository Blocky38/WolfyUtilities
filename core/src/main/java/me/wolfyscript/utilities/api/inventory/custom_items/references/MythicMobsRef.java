package me.wolfyscript.utilities.api.inventory.custom_items.references;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.util.jnbt.CompoundTag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class MythicMobsRef extends APIReference {

    private final String itemName;

    public MythicMobsRef(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public ItemStack getLinkedItem() {
        return MythicMobs.inst().getItemManager().getItemStack(itemName);
    }

    @Override
    public ItemStack getIdItem() {
        return getLinkedItem();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MythicMobsRef)) return false;
        if (!super.equals(o)) return false;
        MythicMobsRef that = (MythicMobsRef) o;
        return Objects.equals(itemName, that.itemName);
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStringField("mythicmobs", itemName);
    }

    public static class Parser extends APIReference.PluginParser<MythicMobsRef> {

        public Parser() {
            super("MythicMobs", "mythicmobs");
        }

        @Override
        public @Nullable MythicMobsRef construct(ItemStack itemStack) {
            if (MythicMobs.inst().getVolatileCodeHandler().getItemHandler() != null) {
                CompoundTag compoundTag = MythicMobs.inst().getVolatileCodeHandler().getItemHandler().getNBTData(itemStack);
                String name = compoundTag.getString("MYTHIC_TYPE");
                if (MythicMobs.inst().getItemManager().getItem(name).isPresent()) {
                    return new MythicMobsRef(name);
                }
            }
            return null;
        }

        @Override
        public @Nullable MythicMobsRef parse(JsonNode element) {
            return new MythicMobsRef(element.asText());
        }
    }
}