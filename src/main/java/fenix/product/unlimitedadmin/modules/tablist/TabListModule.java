package fenix.product.unlimitedadmin.modules.tablist;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.IModule;
import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class TabListModule implements IModule, Listener {
    final UnlimitedAdmin plugin;

    public TabListModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;
        TabListConfig.load();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getName() {
        return "tablist";
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final String formatedText = PlaceHolderUtils.replacePlayerPlaceholders(
                TabListConfig.TABLIST_FORMAT.getString());
        player.setPlayerListName(formatedText);
    }

}
