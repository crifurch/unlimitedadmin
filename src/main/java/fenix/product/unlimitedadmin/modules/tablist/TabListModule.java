package fenix.product.unlimitedadmin.modules.tablist;

import fenix.product.unlimitedadmin.ModulesManager;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.module.IModule;
import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

// todo: support icons
public class TabListModule implements IModule, Listener {
    public TabListModule(UnlimitedAdmin plugin) {
        TabListConfig.load();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public @NotNull String getName() {
        return ModulesManager.TABLIST.getName();
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final String formatedText = PlaceHolderUtils.replacePlayerPlaceholders(
                TabListConfig.TABLIST_FORMAT.getString());
        player.setPlayerListName(formatedText);
    }

}
