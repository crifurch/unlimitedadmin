package fenix.product.unlimitedadmin.modules.world.listeners;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.modules.world.WorldManager;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.logging.Level;

public class WorldListeners implements Listener {
    final UnlimitedAdmin plugin;
    final WorldManager manager;

    public WorldListeners(UnlimitedAdmin plugin, WorldManager manager) {
        this.plugin = plugin;
        this.manager = manager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void loadWorld(WorldLoadEvent event) {
        World world = event.getWorld();
        final String s = manager.loadWorld(world.getName());
        if (s != null) {
            plugin.getLogger().log(Level.WARNING, s);
        }
    }

    @EventHandler
    public void initWorld(WorldInitEvent event) {
        World world = event.getWorld();
        world.setKeepSpawnInMemory(false);
    }
}
