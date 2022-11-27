package fenix.product.unlimitedadmin.api.utils;

import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.utils.HookManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderUtils {
    static final Pattern PLACEHOLDER_SUFFIX = Pattern.compile("%suffix");

    static final Map<Integer, Pattern> PLACEHOLDER_PATTERN_CACHE = new HashMap<>();

    static {
        for (int i = 1; i < 10; i++) {
            PLACEHOLDER_PATTERN_CACHE.put(i, Pattern.compile("%" + i + "[a-zA-Z]+"));
        }
    }

    public static String replacePlayerPlaceholders(String format, @Nullable Player... players) {
        String result = format;

        if (players != null && players.length > 0) {
            for (int i = 0; i < players.length; i++) {
                final Player player = players[i];
                result = makePlaceholdersUnderstand(result, i + 1);
                result = replacePlayerPlaceholder(result, player);
            }
        }
        result = replaceColors(result);

        return result;
    }

    private static String makePlaceholdersUnderstand(String placeholder, int i) {
        final Matcher matcher = PLACEHOLDER_PATTERN_CACHE.get(i).matcher(placeholder);
        while (matcher.find()) {
            final String group = matcher.group();
            final String newGroup = group.substring(0, group.length() - 1);
            placeholder = placeholder.replace(group, newGroup);
        }
        return placeholder;
    }

    public static String replacePlayerPlaceholder(String format, @Nullable Player player) {
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
        return result;
    }

    public static String replaceColors(String message) {
        message = RGBColors.translateCustomColorCodes(message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
