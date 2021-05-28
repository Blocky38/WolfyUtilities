package me.wolfyscript.utilities.api.inventory.custom_items.meta.modules;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import me.wolfyscript.utilities.api.inventory.custom_items.meta.Meta;
import me.wolfyscript.utilities.util.NamespacedKey;
import me.wolfyscript.utilities.util.Registry;

import java.util.HashMap;
import java.util.Map;

public class MetaModule {

    private final HashMap<NamespacedKey, Meta> settings = new HashMap<>();
    private final @JsonProperty
    Type type;

    public MetaModule(Type type) {
        this.type = type;
    }

    public MetaModule(MetaModule module) {
        this.type = module.type;

        module.settings.forEach((key, meta) -> {
            //TODO: Clone Meta!

        });

    }

    public final boolean isApplicable(Meta meta) {
        return meta.getModuleType().equals(type);
    }

    public Type getType() {
        return type;
    }

    @JsonGetter
    public Map<NamespacedKey, Meta> getSettings() {
        return settings;
    }

    @JsonSetter
    private void setSettings(Map<String, JsonNode> settings) {
        settings.forEach((key, value) -> {
            NamespacedKey namespacedKey = key.contains(":") ? NamespacedKey.of(key) : NamespacedKey.wolfyutilties(key);
            Meta.Provider<?> provider = Registry.META_PROVIDER.get(namespacedKey);
            if (provider != null) {
                Meta meta = provider.parse(value);
                if (isApplicable(meta)) {
                    this.settings.put(namespacedKey, meta);
                }
            }
        });
    }

    public MetaModule clone() {
        return new MetaModule(this);
    }

    public enum Type {

        BANNER,
        BLOCK_STATE,
        BOOK,
        BOOK_SIGNED,
        CHARGE,
        COMPASS,
        CROSSBOW,
        ENCHANTED_BOOK,
        ENTITY_TAG,
        FIREWORK,
        ITEM,
        KNOWLEDGE_BOOK,
        LEATHER_ARMOR,
        MAP,
        POTION,
        SKULL,
        SPAWN_EGG,
        SUSPICIOUS_STEW,
        TROPICAL_FISH_BUCKET


    }
}
