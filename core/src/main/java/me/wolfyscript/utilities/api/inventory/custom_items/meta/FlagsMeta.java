package me.wolfyscript.utilities.api.inventory.custom_items.meta;


import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.nms.nbt.NBTItem;
import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder;

public class FlagsMeta extends Meta {

    public FlagsMeta() {
        setOption(MetaSettings.Option.EXACT);
        setAvailableOptions(MetaSettings.Option.EXACT, MetaSettings.Option.IGNORE);
    }

    @Override
    public boolean check(ItemBuilder itemOther, ItemBuilder item) {
        if (option.equals(MetaSettings.Option.IGNORE)) {
            return true;
        }
        NBTItem itemThis = WolfyUtilities.getWUCore().getNmsUtil().getNBTUtil().getItem(item.create());
        NBTItem itemThat = WolfyUtilities.getWUCore().getNmsUtil().getNBTUtil().getItem(itemOther.create());
        return itemThis.getCompound().getInt("HideFlags") == itemThat.getCompound().getInt("HideFlags");
    }
}
