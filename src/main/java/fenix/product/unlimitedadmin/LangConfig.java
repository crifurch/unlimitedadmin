package fenix.product.unlimitedadmin;

import fenix.product.unlimitedadmin.api.utils.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public enum LangConfig {
    ERROR_WHILE_COMMAND("command.errorWhileExecute", "Error when executing command"),
    ONLY_FOR_PLAYER_COMMAND("command.onlyForUser", "Only player can run this command whit this arguments"),
    NO_SUCH_PLAYER("player.notFound", "No such player"),
    OFFLINE_PLAYER("player.isOffline", "Player is offline"),
    NO_SUCH_WORLD("no_such_world", "No such world"),
    NO_PERMISSIONS_USE_ON_OTHER("no_permissions_use_on_other", "You can not use this command on this player"),
    SERVER_IN_MAINTAIN_MODE("server_in_maintain_mode", "Server in maintain mode now"),
    SERVER_NOT_IN_MAINTAIN_MODE("server_in_maintain_mode", "Server in work mode now"),
    WORLD_LOCKED_FOR_ENTERING("world_locked_for_entering", "World locked for entering"),
    NO_WORLD_FOUND("no_world_found", "No world found"),
    WORLD_IS_LOCKED("world_is_locked", "World %s is locked"),
    WORLD_IS_UNLOCKED("world_is_unlocked", "World %s is unlocked"),
    DONATION_AMOUNT("donation_amount", "Caps amount: %s"),
    NO_COMMAND_FOUND("no_command_found", "No command found"),

    HOME_CREATED("home.created", "Home %s was created"),
    NO_SUCH_HOME("home.notFound", "Home %s not found"),
    HOME_DELETED("home.deleted", "Home %s was deleted");


    private final String value;
    private final String path;
    private static YamlConfiguration cfg;
    private static final File f = FileUtils.getFileFromList(UnlimitedAdmin.getInstance().getDataFolder(), Collections.singletonList("lang.yml"));

    LangConfig(String path, String val) {
        this.path = path;
        this.value = val;
    }

    public String getText(Object... args) {
        final String string = cfg.getString(path);
        if (string == null) {
            return value;
        }
        return String.format(string, args);
    }

    public String getText() {
        final String string = cfg.getString(path);
        if (string == null) {
            return value;
        }
        return string;
    }

    @Override
    public String toString() {
        return getText();
    }

    public static void load() {
        reload(false);
        save();
    }

    public static void save() {
        for (LangConfig c : values()) {
            if (!cfg.contains(c.path)) {
                c.set(c.value, false);
            }
        }
        try {
            cfg.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void set(Object value, boolean save) {
        cfg.set(path, value);
        if (save) {
            try {
                cfg.save(f);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            reload(false);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void reload(boolean complete) {
        if (!complete) {
            f.getParentFile().mkdirs();
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cfg = YamlConfiguration.loadConfiguration(f);
            return;
        }
        load();
    }
}
