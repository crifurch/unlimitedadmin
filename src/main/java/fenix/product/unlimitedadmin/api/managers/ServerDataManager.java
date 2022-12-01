package fenix.product.unlimitedadmin.api.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.stream.Collectors;

public class ServerDataManager {
    private static String mainWorldName;
    private static Boolean isOnlineMode;

    @Nullable
    public static String getServerProperty(String key) {
        File file = new File("server.properties");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(key)) {
                    return line.split("=")[1];
                }
            }
        } catch (Exception e) {
            //do nothing
        }
        return null;
    }

    @NotNull
    public static String getServerProperty(String key, String defaultValue) {
        final String serverProperty = getServerProperty(key);
        if (serverProperty != null) {
            return serverProperty;
        }
        return defaultValue;
    }

    public static String getMainWorldName() {
        if (mainWorldName == null) {
            mainWorldName = getServerProperty("level-name", "overworld");
        }
        return mainWorldName;
    }

    public static boolean isServerInOnlineMode() {
        if (isOnlineMode == null) {
            isOnlineMode = Boolean.parseBoolean(getServerProperty("online-mode", "true"));
        }
        return isOnlineMode;
    }

    public static Collection<String> getOPs(boolean online) {
        return Bukkit.getOperators().stream().filter(offlinePlayer -> offlinePlayer.isOnline() == online)
                .map(OfflinePlayer::getName).collect(Collectors.toList());
    }

    public static Collection<String> getAllOPs() {
        return Bukkit.getOperators().stream().map(OfflinePlayer::getName).collect(Collectors.toList());
    }

    public static void setOP(String player, boolean op) {
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        offlinePlayer.setOp(op);
    }
}
