package fenix.product.unlimitedadmin.modules.playersmap;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.IModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class PlayersMapModule implements IModule, Listener {
    private Map<String, String> playerMap = new HashMap<>();
    private final File mapFile;

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        if (playerMap.containsKey(player.getName()) && Objects.equals(playerMap.get(player.getName()), player.getUniqueId().toString())) {
            return;
        }
        playerMap.put(player.getName(), player.getUniqueId().toString());
        try {
            save();
        } catch (IOException e) {
            playerMap.remove(player.getName());
            e.printStackTrace();
        }
    }

    public PlayersMapModule(UnlimitedAdmin plugin) {
        mapFile = new File(plugin.getDataFolder().getAbsolutePath() + "/playerMap.csv");
        if (!mapFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                mapFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void load() throws IOException {
        playerMap.clear();
        final BufferedReader fileReader = new BufferedReader(new FileReader(mapFile));
        String currentLine;
        while ((currentLine = fileReader.readLine()) != null) {
            if (!currentLine.contains(",")) {
                break;
            }
            final String[] split = currentLine.split(",");
            playerMap.put(split[0], split[1]);
        }
        fileReader.close();
    }

    private void save() throws IOException {
        final BufferedWriter fileWriter = new BufferedWriter(new FileWriter(mapFile));
        final boolean[] isFirst = {true};
        playerMap.forEach((s, s2) -> {
            try {
                if (isFirst[0]) {
                    isFirst[0] = false;
                } else {
                    fileWriter.newLine();
                }
                fileWriter.write(s + "," + s2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileWriter.close();
    }

    public Collection<String> getPlayers() {
        return playerMap.keySet();
    }

    public Iterable<String> getPlayersUUID() {
        return playerMap.values();
    }

    @Nullable
    public UUID getPlayerUUID(String name) {
        return UUID.fromString(playerMap.get(name));
    }

    @Override
    public String getName() {
        return "playermap";
    }
}
