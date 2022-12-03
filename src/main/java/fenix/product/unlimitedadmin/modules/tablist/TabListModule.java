package fenix.product.unlimitedadmin.modules.tablist;

import fenix.product.unlimitedadmin.ModulesManager;
import fenix.product.unlimitedadmin.api.modules.AdminModule;
import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

// todo: support icons
public class TabListModule extends AdminModule implements Listener {
    public TabListModule() {
    }

    @Override
    public @NotNull String getName() {
        return ModulesManager.TABLIST.getName();
    }


    @Override
    public void onEnable() {
        TabListConfig.load();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public Collection<Listener> getListeners() {
        return Collections.singletonList(this);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        updateTabList(player);
    }

    private void updateTabList(Player player) {
        final String formatedText = PlaceHolderUtils.replacePlayerPlaceholders(
                TabListConfig.TABLIST_FORMAT.getString());
        player.setPlayerListName(formatedText);
    }

}
