package fenix.product.unlimitedadmin.modules.tablist.utils;

import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import org.bukkit.entity.Player;

public class TabListPlaceHolderUtils {
    public static String replacePlayerPlaceholders(Player player, String format) {
        if (player == null) {
            return format;
        }
        String result = format;
        result = result.replace("%displayname", player.getDisplayName());
        result = result.replace("%prefix", PermissionsProvider.getInstance().getPrefix(player));
        result = result.replace("%suffix", PermissionsProvider.getInstance().getSuffix(player));
        result = result.replace("%player", player.getName());
        result = result.replace("%world", player.getWorld().getName());
        return result;
    }
}
