package fenix.product.unlimitedadmin.modules.playersmap;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.IModule;
import fenix.product.unlimitedadmin.modules.playersmap.data.CachedPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import javax.annotation.Nullable;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;

public class PlayersMapModule implements IModule, Listener {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final List<CachedPlayer> playerMap = new ArrayList<>();
    private final File mapFile;

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        LocalDateTime now = LocalDateTime.now();
        CachedPlayer setted = null;
        for (CachedPlayer i : playerMap) {
            if (i.uuid.equals(player.getUniqueId().toString())) {
                setted = i;
                break;
            }
        }
        if (setted == null) {
            final CachedPlayer cachedPlayer = new CachedPlayer(player.getUniqueId().toString(), player.getName(), now);
            playerMap.add(cachedPlayer);
        } else {
            setted.lastSign = now;
        }
        playerMap.sort(Comparator.comparing(o -> ((CachedPlayer) o).lastSign).reversed());
        try {
            save();
        } catch (IOException e) {
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
        LocalDateTime now = LocalDateTime.now();
        while ((currentLine = fileReader.readLine()) != null) {
            if (!currentLine.contains(",")) {
                break;
            }
            final String[] split = currentLine.split(",");
            //todo remove after servers migrate to new plugin
            final String text;
            if (split.length >= 3) {
                text = split[2];
            } else {
                text = dtf.format(now);
            }

            playerMap.add(new CachedPlayer(split[1], split[0], LocalDateTime.parse(text, dtf)));
        }
        fileReader.close();
        playerMap.sort(Comparator.comparing(o -> ((CachedPlayer) o).lastSign).reversed());
    }

    private void save() throws IOException {
        final BufferedWriter fileWriter = new BufferedWriter(new FileWriter(mapFile));
        final boolean[] isFirst = {true};
        playerMap.forEach(cachedPlayer -> {
            try {
                if (isFirst[0]) {
                    isFirst[0] = false;
                } else {
                    fileWriter.newLine();
                }
                fileWriter.write(cachedPlayer.name + "," + cachedPlayer.uuid + "," + dtf.format(cachedPlayer.lastSign));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileWriter.close();
    }

    public Collection<CachedPlayer> getPlayers() {
        return new ArrayList<>(playerMap);
    }

    public Iterable<UUID> getPlayersUUID() {
        return playerMap.stream().map(cachedPlayer -> UUID.fromString(cachedPlayer.uuid)).collect(Collectors.toList());
    }

    @Nullable
    public UUID getPlayerUUID(String name) {
        for (CachedPlayer p : playerMap) {
            if (p.name.equals(name)) {
                return UUID.fromString(p.uuid);
            }
        }
        return null;
    }

    @Nullable
    public String getPlayerName(UUID id) {
        for (CachedPlayer p : playerMap) {
            if (p.uuid.equals(id.toString())) {
                return p.name;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "playermap";
    }
}
