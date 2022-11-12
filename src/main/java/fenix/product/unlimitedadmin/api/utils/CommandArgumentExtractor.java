package fenix.product.unlimitedadmin.api.utils;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CommandArgumentExtractor {
    @Nullable
    public static <T> T extractArgument(List<String> args, int index, Class<T> type) {
        if (args.size() <= index) {
            return null;
        }
        String arg = args.get(index);
        try {
            if (type == String.class) {
                return (T) arg;
            }
            if (type == Integer.class) {
                return (T) Integer.valueOf(arg);
            }
            if (type == Double.class) {
                return (T) Double.valueOf(arg);
            }
            if (type == Float.class) {
                return (T) Float.valueOf(arg);
            }
            if (type == Long.class) {
                return (T) Long.valueOf(arg);
            }
            if (type == Short.class) {
                return (T) Short.valueOf(arg);
            }
            if (type == Byte.class) {
                return (T) Byte.valueOf(arg);
            }
            if (type == Boolean.class) {
                return (T) Boolean.valueOf(arg);
            }
            if (type == Player.class) {
                return (T) Bukkit.getPlayer(arg);
            }
            if (type == UUID.class) {
                return (T) UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerUUID(arg);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}