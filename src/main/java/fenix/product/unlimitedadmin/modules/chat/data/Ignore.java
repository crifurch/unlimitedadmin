package fenix.product.unlimitedadmin.modules.chat.data;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.UUID;

public class Ignore {
    private final UUID uuid;
    private final List<String> ignoredPlayers;

    public Ignore(UUID uuid, List<String> ignoredPlayers) {
        this.uuid = uuid;
        this.ignoredPlayers = ignoredPlayers;
    }

    public static Ignore fromConfigurationSection(ConfigurationSection section) {
        final UUID uuid = UUID.fromString(section.getName());
        final List<String> ignoredPlayers = section.getStringList("ignoredPlayers");
        return new Ignore(uuid, ignoredPlayers);
    }

    public UUID getUUID() {
        return uuid;
    }

    public List<String> getIgnoredPlayers() {
        return ignoredPlayers;
    }

    public void addIgnoredPlayer(String player) {
        if (ignoredPlayers.contains(player)) {
            return;
        }
        ignoredPlayers.add(player);
    }

    public void removeIgnoredPlayer(String player) {
        ignoredPlayers.remove(player);
    }

    public ConfigurationSection toConfigurationSection(ConfigurationSection section) {
        section.set("ignoredPlayers", ignoredPlayers);
        return section;
    }
}
