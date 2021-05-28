package me.wolfyscript.utilities.api.inventory.custom_items.meta;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;
import me.wolfyscript.utilities.util.NamespacedKey;
import me.wolfyscript.utilities.util.Registry;
import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;
import me.wolfyscript.utilities.util.json.jackson.JacksonUtil;

import java.util.HashMap;
import java.util.Locale;

public class MetaSettings {

    private final HashMap<NamespacedKey, Meta> settings = new HashMap<>();

    @JsonAnySetter
    public void set(String fieldName, JsonNode value) {
        String key = fieldName.toLowerCase(Locale.ROOT);
        NamespacedKey namespacedKey = key.contains(":") ? NamespacedKey.of(key) : NamespacedKey.wolfyutilties(key);
        Meta.Provider<?> provider = Registry.META_PROVIDER.get(namespacedKey);
        if (provider != null) {
            Meta meta;
            if (value.isTextual()) {
                meta = provider.provide();
                meta.setOption(JacksonUtil.getObjectMapper().convertValue(value, MetaSettings.Option.class));
            } else {
                meta = provider.parse(value);
            }
            settings.put(namespacedKey, meta);
        }
    }

    public boolean check(ItemBuilder itemOther, ItemBuilder item) {
        return settings.values().stream().allMatch(meta -> meta.check(itemOther, item));
    }

    public enum Option {
        EXACT, IGNORE, HIGHER, HIGHER_EXACT, LOWER, LOWER_EXACT, HIGHER_LOWER
    }
}
