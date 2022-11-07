package fenix.product.unlimitedadmin.modules.spawn.listeners;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.modules.spawn.SpawnModule;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnDeathListener implements Listener {
    final SpawnModule module;

    public SpawnDeathListener(SpawnModule module) {
        this.module = module;
    }

    @EventHandler
    public void onPlayerDeath(PlayerRespawnEvent event) {
        final Location spawnLocation = module.getSpawnLocation(GlobalConstants.defaultEntryName);
        if(spawnLocation != null) {
            event.setRespawnLocation(spawnLocation);
        }
    }
}
