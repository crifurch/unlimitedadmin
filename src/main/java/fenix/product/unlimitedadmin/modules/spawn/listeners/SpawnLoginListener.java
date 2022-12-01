package fenix.product.unlimitedadmin.modules.spawn.listeners;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.api.utils.PlayerUtils;
import fenix.product.unlimitedadmin.modules.playersmap.data.PlayerFirstJoinEvent;
import fenix.product.unlimitedadmin.modules.spawn.SpawnModule;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SpawnLoginListener implements Listener {
    final SpawnModule module;

    public SpawnLoginListener(SpawnModule module) {
        this.module = module;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerFirstJoinEvent event) {
        final Location spawnLocation = module.getSpawnLocation(GlobalConstants.defaultEntryName);
        if (spawnLocation != null) {
            PlayerUtils.setLocationDelayed(event.getPlayer(), spawnLocation, 400);
        }
    }
}
