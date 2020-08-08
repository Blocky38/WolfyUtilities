package me.wolfyscript.utilities.api.utils.protection;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.api.PlotAPI;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PSUtils {

    //PlotSquared API Utils
    private static final PlotAPI plotAPI = new PlotAPI();

    @Deprecated
    public static boolean hasPerm(Player player, org.bukkit.Location location) {
        Location location1 = BukkitUtil.getLocation(location);
        Plot plot = location1.getPlot();
        if (!isPlotWorld(player.getWorld()))
            return true;
        return hasPlotPerm(player.getUniqueId(), plot);
    }

    public static boolean isPlotWorld(World world) {
        return plotAPI.getPlotSquared().hasPlot(world.getUID());
    }

    public static boolean hasPlotPerm(UUID uuid, Plot plot) {
        return plot != null && (plot.getTrusted().contains(uuid) || plot.isOwner(uuid) || plot.getMembers().contains(uuid) || plot.isAdded(uuid));
    }

}
