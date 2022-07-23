package fenix.product.unlimitedadmin;

import fenix.product.unlimitedadmin.api.utils.FileUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public enum UnlimitedAdminConfig {
    TABLIST_MODULE_ENABLED("modules.tablist", true, "TabList Module provide custom appearence of ingame player tab list"),
    WORLDS_MODULE_ENABLED("modules.worldManager", true, "WorldManager Module provide abilities to create custom worlds"),
    TELEPORT_MODULE_ENABLED("modules.teleport", true, "Teleport Module provide abilities teleporting online/offline players"),
    SPAWN_MODULE_ENABLED("modules.spawn", true, "Create spawn points on your server"),
    HOME_MODULE_ENABLED("modules.home", true, "Create home points on your server"),
    PLAYER_STATUS_MODULE_ENABLED("modules.playerstatus", true, "Manipulating whit player status");

    private final Object value;
    private final String path;
    private final String description;
    private static YamlConfiguration cfg;
    private static final File f = FileUtils.getFileFromList(UnlimitedAdmin.getInstance().getDataFolder(), Collections.singletonList("config.yml"));

    UnlimitedAdminConfig(String path, Object val, String description) {
        this.path = path;
        this.value = val;
        this.description = description;
    }

    public boolean getBoolean() {
        return cfg.getBoolean(path);
    }


    public ConfigurationSection getConfigurationSection() {
        return cfg.getConfigurationSection(path);
    }

    public static void load() {
        reload(false);
        save();
    }

    public static void save() {
        StringBuilder header = new StringBuilder();
        header.append("Changes in this file will may effect when restart server, command /reload reload only active modules");
        for (UnlimitedAdminConfig c : values()) {
            header.append(c.path).append(": ").append(c.description).append(System.lineSeparator());
            if (!cfg.contains(c.path)) {
                c.set(c.value, false);
            }
        }
        cfg.options().header(header.toString());
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
