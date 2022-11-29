package fenix.product.unlimitedadmin;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.ModuleConfig;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.IModule;
import fenix.product.unlimitedadmin.api.utils.CommandExecutor;
import fenix.product.unlimitedadmin.api.utils.CommandsRegister;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.core.UnlimitedAdminExecutor;
import fenix.product.unlimitedadmin.modules.home.HomeModule;
import fenix.product.unlimitedadmin.modules.maintain.MaintainModule;
import fenix.product.unlimitedadmin.modules.player_status.PlayerStatusModule;
import fenix.product.unlimitedadmin.modules.playersmap.PlayersMapModule;
import fenix.product.unlimitedadmin.modules.shop.ShopModule;
import fenix.product.unlimitedadmin.modules.spawn.SpawnModule;
import fenix.product.unlimitedadmin.modules.tablist.TabListModule;
import fenix.product.unlimitedadmin.modules.teleporting.TeleportingModule;
import fenix.product.unlimitedadmin.modules.world.WorldManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

    private String mainWorldName = "overworld";

    @Override
    public void onEnable() {
        INSTANCE = this;
        CommandsRegister.init(this);
        UnlimitedAdminConfig.load();
        LangConfig.load();
        commandExecutor = new UnlimitedAdminExecutor(this);
        setMainWoldName();
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
        if (UnlimitedAdminConfig.MAINTAIN_MODULE_ENABLED.getBoolean()) {
            modules.add(new MaintainModule(this));
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
        if (UnlimitedAdminConfig.SHOP_MODULE_ENABLED.getBoolean()) {
            rawModules.add(new ShopModule(this));
        }
        if (UnlimitedAdminConfig.CHAT_MODULE_ENABLED.getBoolean()) {
            rawModules.add(new ChatModule(this));
        }


        for (IModule module : rawModules) {
            for (ICommand command : module.getCommands()) {
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

    public File getModuleConfigFile(IModule module) {
        return getModuleConfigFile(module, "config");
    }

    public File getModuleConfigFile(IModule module, String name) {
        return getModuleFile(module, name + ".yml");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File getModuleFile(IModule module, String name) {
        final File moduleFolder = getModuleFolder(module);
        if (!moduleFolder.exists()) {
            moduleFolder.mkdirs();
        }
        final File config = new File(moduleFolder, name);
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

    private void setMainWoldName() {
        final File file = new File("server.properties");
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("level-name")) {
                        mainWorldName = line.split("=")[1];
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getMainWorldName() {
        return mainWorldName;
    }

}
