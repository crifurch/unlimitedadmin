package fenix.product.unlimitedadmin.modules.spawn;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.IModule;
import fenix.product.unlimitedadmin.api.utils.FileUtils;
import fenix.product.unlimitedadmin.modules.spawn.commands.DelSpawnCommand;
import fenix.product.unlimitedadmin.modules.spawn.commands.ListSpawnCommand;
import fenix.product.unlimitedadmin.modules.spawn.commands.SetSpawnCommand;
import fenix.product.unlimitedadmin.modules.spawn.commands.SpawnCommand;
import fenix.product.unlimitedadmin.modules.spawn.listeners.SpawnDeathListener;
import fenix.product.unlimitedadmin.modules.spawn.listeners.SpawnLoginListener;
import fenix.product.unlimitedadmin.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpawnModule implements IModule {

    private static YamlConfiguration cfg;
    private static File f;
    final UnlimitedAdmin plugin;
    private final List<ICommand> commands = new ArrayList<>();

    public SpawnModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;
        f = FileUtils.getFileFromList(UnlimitedAdmin.getInstance().getModuleFolder(this), Collections.singletonList("spawns.yml"));
        cfg = YamlConfiguration.loadConfiguration(f);
        SpawnModuleConfig.load();
        commands.add(new SetSpawnCommand(this));
        commands.add(new SpawnCommand(this));
        commands.add(new DelSpawnCommand(this));
        commands.add(new ListSpawnCommand(this));
        if (SpawnModuleConfig.PREFER_TELEPORT_ON_DEATH.getBoolean()) {
            plugin.getServer().getPluginManager().registerEvents(new SpawnDeathListener(this), plugin);
        }
        if (SpawnModuleConfig.PREFERRED_FIRST_SPAWN.getBoolean()) {
            plugin.getServer().getPluginManager().registerEvents(new SpawnLoginListener(this), plugin);
        }
    }

    @Override
    public String getName() {
        return "spawn";
    }

    public void setSpawn(@NotNull String name, @NotNull Location position) {
        if (!cfg.contains(name)) {
            cfg.createSection(name);
        }
        cfg.set(name, position);
        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteSpawn(@NotNull String name) {
        if (cfg.contains(name)) {
            cfg.set(name, null);
        }
        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getSpawns() {
        return cfg.getValues(false).keySet();
    }

    public Location getSpawnLocation(String name) {
        return cfg.getLocation(name);
    }

    public boolean teleportPlayerToSpawn(@NotNull UUID player, @NotNull String spawnName) {
        if (cfg.contains(spawnName)) {
            PlayerUtils.setLocation(player, getSpawnLocation(spawnName));
            return true;
        }
        return false;
    }


    @Override
    public List<ICommand> getCommands() {
        return commands;
    }
}
