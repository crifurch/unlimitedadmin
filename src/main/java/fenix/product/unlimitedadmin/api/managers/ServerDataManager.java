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

    @SuppressWarnings("rawtypes")
    public static void setOP(String player, boolean op) {
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        offlinePlayer.setOp(op);
        File file = new File("ops.json");
//        StringBuilder sb = new StringBuilder();
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//                sb.append(System.lineSeparator());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//        String json = sb.toString();
//        UnlimitedAdmin.getInstance().getLogger().info(json);
//        Gson gson = new Gson();
//        HashMap[] ops = gson.fromJson(json, HashMap[].class);
//        boolean found = false;
//        for (HashMap opMap : ops) {
//            if (opMap.get("name").equals(player)) {
//                found = true;
//                break;
//            }
//        }
//        if (op) {
//            //add op
//            if (found) {
//                return;
//            }
//            final int level = Integer.parseInt(getServerProperty("op-permission-level", "4"));
//            HashMap<String, Object> newOp = new HashMap<>();
//            newOp.put("name", offlinePlayer.getName());
//            newOp.put("uuid", offlinePlayer.getUniqueId().toString());
//            newOp.put("level", level);
//            newOp.put("bypassesPlayerLimit", false);
//            ops = Arrays.copyOf(ops, ops.length + 1);
//            ops[ops.length - 1] = newOp;
//        } else {
//            if (!found) {
//                return;
//            }
//            ops = Arrays.stream(ops).filter(opMap -> !opMap.get("name").equals(player)).toArray(HashMap[]::new);
//        }
//        json = gson.toJson(ops);
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
//            bw.write(json);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //do nothing
//        }

    }
}
