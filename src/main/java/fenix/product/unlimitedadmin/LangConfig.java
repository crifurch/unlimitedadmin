package fenix.product.unlimitedadmin;

import fenix.product.unlimitedadmin.api.utils.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public enum LangConfig {
    ERROR_WHILE_COMMAND("error_while_command", "Error when executing command"),
    NO_SUCH_PLAYER("no_such_player", "No such player"),
    NO_PERMISSIONS_USE_ON_OTHER("no_permissions_use_on_other", "You can not use this command on this player"),
    ONLY_FOR_PLAYER_COMMAND("only_for_player_command", "Only player can run this command whit this arguments");


    private final String value;
    private final String path;
    private static YamlConfiguration cfg;
    private static final File f = FileUtils.getFileFromList(UnlimitedAdmin.getInstance().getDataFolder(), Collections.singletonList("lang.yml"));

    LangConfig(String path, String val) {
        this.path = path;
        this.value = val;
    }

    public String getText() {
        return cfg.getString(path);
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
