package fenix.product.unlimitedadmin.modules.spawn.listeners;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.modules.spawn.SpawnModule;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpawnLoginListener implements Listener {
    final SpawnModule module;

    public SpawnLoginListener(SpawnModule module) {
        this.module = module;
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        final Location spawnLocation = module.getSpawnLocation(GlobalConstants.defaultEntryName);
        if(spawnLocation != null) {
            event.getPlayer().teleport(spawnLocation);
        }
    }
}
