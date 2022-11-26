package fenix.product.unlimitedadmin.api.utils;

import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.utils.HookManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlaceHolderUtils {
    public static String replacePlayerPlaceholders(@Nullable Player player, String format) {
        String result = format;

        if (HookManager.checkPlaceholderAPI() && player != null) {
            try {
                final Class<?> aClass = ClassLoader.getSystemClassLoader().loadClass("me.clip.placeholderapi.PlaceholderAPI");
                result = (String) aClass.getMethod("setPlaceholders", Player.class, String.class).invoke(null, player, format);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        if ((HookManager.checkEssentials() || HookManager.checkPurpur()) && Config.AFK_PLACEHOLDER.getBoolean()) {
//            result = result.replace("%afk", "");
//        }

        result = result.replace("%displayname", player == null ? "" : player.getDisplayName());
        result = result.replace("%prefix", player == null ? "" : PermissionsProvider.getInstance().getPrefix(player));
        result = result.replace("%suffix", player == null ? "" : PermissionsProvider.getInstance().getSuffix(player));
        result = result.replace("%player", player == null ? "" : player.getName());
        result = result.replace("%world", player == null ? "" : player.getWorld().getName());
        result = result.replace("%group", player == null ? "" : PermissionsProvider.getInstance().getGroupNames(player).length > 0 ? PermissionsProvider.getInstance().getGroupNames(player)[0] : "none");
        result = replaceColors(result);

        return result;
    }

    public static String replaceColors(String message) {
        message = RGBColors.translateCustomColorCodes(message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
