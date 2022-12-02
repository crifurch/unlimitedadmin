package fenix.product.unlimitedadmin.modules.world;

import fenix.product.unlimitedadmin.ModulesManager;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.module.IModule;
import fenix.product.unlimitedadmin.api.utils.FileUtils;
import fenix.product.unlimitedadmin.modules.world.commands.CreateCommand;
import fenix.product.unlimitedadmin.modules.world.commands.DeleteCommand;
import fenix.product.unlimitedadmin.modules.world.commands.GuiCommand;
import fenix.product.unlimitedadmin.modules.world.commands.ListCommand;
import fenix.product.unlimitedadmin.modules.world.listeners.WorldListeners;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// todo add load worlds
public class WorldsModule implements IModule {
    private final UnlimitedAdmin plugin;
    private final File worldsMapFolder;
    private final boolean inited;
    private final Map<String, FileConfiguration> worldsConfigurations;
    private final List<ICommand> commands;


    public WorldsModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;
        worldsMapFolder = new File(plugin.getDataFolder().getAbsolutePath() + "/worlds/map");
        if (!worldsMapFolder.exists()) {
            final Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "Ok no worlds was created, i will create folder");
            if (!worldsMapFolder.mkdirs()) {
                logger.log(Level.WARNING, "I believe that folder " + worldsMapFolder.getAbsolutePath() + " will be crated later, disabling module worlds");
                inited = false;
            } else {
                inited = true;
            }
        } else {
            inited = true;
        }
        worldsConfigurations = new HashMap<>();
        for (File file : Objects.requireNonNull(worldsMapFolder.listFiles())) {
            if (file.getName().endsWith("_world.yml")) {
                worldsConfigurations.put(file.getName().substring(0, file.getName().length() - 10)
                        , YamlConfiguration.loadConfiguration(file));
            }
        }
        commands = Arrays.asList(
                new CreateCommand(this),
                new DeleteCommand(this),
                new GuiCommand(this),
                new ListCommand(this)
        );

        new WorldListeners(plugin, this);

        for (String name : worldsConfigurations.keySet()) {
            loadWorld(name);
        }


    }

    @Override
    public @NotNull String getName() {
        return ModulesManager.WORLDS.getName();
    }

    public Collection<String> getWorlds() {
        return worldsConfigurations.keySet();
    }

    public String createWorld(String name, World.Environment type, WorldType worldType) {
        return createWorld(name, type, worldType, 0, false);
    }

    public String createWorld(String name, World.Environment type, WorldType worldType, long seed, boolean ignoreExists) {
        if (!inited) {
            return "Hmmm, why you try create world if module worlds is disabled";
        }
        if (this.plugin.getServer().getWorld(name) != null) {
            return "World with name " + name + " already exists";
        } else if (worldsConfigurations.containsKey(name) && !ignoreExists) {
            return loadWorld(name);
        }

        WorldCreator wc = new WorldCreator(name);

        wc.environment(type);
        wc.type(worldType);
        if (seed != 0) {
            wc.seed(seed);
        }
        World world = null;
        try {
            world = plugin.getServer().createWorld(wc);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (world == null) {
            return "Error when creating world";
        }
        return saveWorld(name);
    }

    public String deleteWorld(String name) {
        if (!inited) {
            return "Hmmm, why you try delete world if module worlds is disabled";
        }
        if (!worldsConfigurations.containsKey(name)) {
            return "This world don't crated by plugin";
        }
        World world = this.plugin.getServer().getWorld(name);
        if (world == null) {
            return "Target world don't exists";
        }
        removePlayersFromWorld(world);
        this.plugin.getServer().unloadWorld(world, false);
        final File worldFolder = world.getWorldFolder();
        FileUtils.deleteFolder(worldFolder);
        return null;
    }

    private String saveWorld(String name) {
        final World world = Bukkit.getServer().getWorld(name);
        final File worldConfig = new File(worldsMapFolder.getAbsolutePath() + "/" + Objects.requireNonNull(world).getName() + "_world.yml");


        if (!worldConfig.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                worldConfig.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return "Error saving to config";
            }
        }
        worldsConfigurations.put(name, new YamlConfiguration());
        final FileConfiguration configuration = worldsConfigurations.get(name);
        configuration.set("seed", world.getSeed());
        String worldType = "NORMAL";
        if (world.getWorldType() != null) {
            worldType = world.getWorldType().toString();
        }
        configuration.set("type", worldType);
        configuration.set("env", world.getEnvironment().toString());
        try {
            configuration.save(worldConfig);
        } catch (IOException e) {
            worldsConfigurations.remove(name);
            if (worldConfig.exists()) {
                //noinspection ResultOfMethodCallIgnored
                worldConfig.delete();
            }
            e.printStackTrace();
            return "Can't save world configurations";
        }
        return null;
    }

    @Nullable
    public String loadWorld(String name) {
        if (plugin.getServer().getWorlds().stream().anyMatch(world -> world.getName().equals(name))) {
            return "World loaded now";
        }
        if (!worldsConfigurations.containsKey(name)) {
            return "Unknown world";
        }

        final FileConfiguration world = worldsConfigurations.get(name);
        return createWorld(name, World.Environment.valueOf(world.getString("env", "NORMAL")),
                WorldType.valueOf(world.getString("type", "NORMAL")), world.getLong("seed", 0), true);
    }

    public void removePlayersFromWorld(World world) {
        if (world != null) {
            World safeWorld = this.plugin.getServer().getWorlds().get(0);
            List<Player> ps = world.getPlayers();
            for (Player p : ps) {
                p.teleport(safeWorld.getSpawnLocation());
            }
        }
    }

    @Override
    public List<ICommand> getCommands() {
        return commands;
    }

}
