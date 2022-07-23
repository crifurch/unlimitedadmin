package fenix.product.unlimitedadmin;

import fenix.product.unlimitedadmin.api.ModuleConfig;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.IModule;
import fenix.product.unlimitedadmin.api.utils.CommandExecutor;
import fenix.product.unlimitedadmin.api.utils.CommandsRegister;
import fenix.product.unlimitedadmin.modules.core.UnlimitedAdminExecutor;
import fenix.product.unlimitedadmin.modules.home.HomeModule;
import fenix.product.unlimitedadmin.modules.player_status.PlayerStatusModule;
import fenix.product.unlimitedadmin.modules.playersmap.PlayersMapModule;
import fenix.product.unlimitedadmin.modules.spawn.SpawnModule;
import fenix.product.unlimitedadmin.modules.tablist.TabListModule;
import fenix.product.unlimitedadmin.modules.teleporting.TeleportingModule;
import fenix.product.unlimitedadmin.modules.world.WorldManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class UnlimitedAdmin extends JavaPlugin {
    private static UnlimitedAdmin INSTANCE;

    private UnlimitedAdminExecutor commandExecutor;


    private final List<IModule> modules = new ArrayList<>();
    private final List<IModule> rawModules = new ArrayList<>();

    public static UnlimitedAdmin getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        CommandsRegister.init(this);
        UnlimitedAdminConfig.load();
        LangConfig.load();
        commandExecutor = new UnlimitedAdminExecutor(this);
        loadModules();
    }

    @Override
    public void onDisable() {
    }

    private void loadModules() {
        PlayersMapModule playersMapModule = new PlayersMapModule(this);
        rawModules.add(playersMapModule);
        //optional modules
        if (UnlimitedAdminConfig.WORLDS_MODULE_ENABLED.getBoolean()) {
            modules.add(new WorldManager(this));
        }
        //raw optional nodules
        if (UnlimitedAdminConfig.TABLIST_MODULE_ENABLED.getBoolean()) {
            rawModules.add(new TabListModule(this));
        }
        if (UnlimitedAdminConfig.TELEPORT_MODULE_ENABLED.getBoolean()) {
            rawModules.add(new TeleportingModule(this));
        }
        if (UnlimitedAdminConfig.SPAWN_MODULE_ENABLED.getBoolean()) {
            rawModules.add(new SpawnModule(this));
        }
        if (UnlimitedAdminConfig.HOME_MODULE_ENABLED.getBoolean()) {
            rawModules.add(new HomeModule(this));
        }
        if (UnlimitedAdminConfig.PLAYER_STATUS_MODULE_ENABLED.getBoolean()) {
            rawModules.add(new PlayerStatusModule(this));
        }

        for (IModule module : rawModules) {
            getLogger().info("We try register command for module " + module.getName());
            for (ICommand command : module.getCommands()) {
                getLogger().info("We try register command " + command.getName());
                final PluginCommand pluginCommand = CommandsRegister.getInstance().registerCommand(command);
                pluginCommand.setExecutor(new CommandExecutor(this, command));

            }
        }
    }

    public List<IModule> getModules() {
        return modules;
    }

    public PlayersMapModule getPlayersMapModule() {
        return (PlayersMapModule) rawModules.get(0);
    }

    public File getModuleFolder(IModule module) {
        return new File(getDataFolder(), module.getName());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File getModuleConfigFile(IModule module) {
        final File moduleFolder = getModuleFolder(module);
        if (!moduleFolder.exists()) {
            moduleFolder.mkdirs();
        }
        final File config = new File(moduleFolder, "config.yml");
        if (!config.exists()) {
            try {
                config.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public ModuleConfig getModuleConfig(IModule module) {
        return new ModuleConfig(getModuleConfigFile(module));
    }
}
