package fenix.product.unlimitedadmin;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.ModuleConfig;
import fenix.product.unlimitedadmin.api.events.ExternalModuleInitEvent;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.ICommandGroup;
import fenix.product.unlimitedadmin.api.interfaces.module.IModule;
import fenix.product.unlimitedadmin.api.managers.CustomCommandsManager;
import fenix.product.unlimitedadmin.api.utils.CommandExecutor;
import fenix.product.unlimitedadmin.modules.playersmap.PlayersMapModule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class UnlimitedAdmin extends JavaPlugin {
    private static UnlimitedAdmin INSTANCE;
    private final List<IModule> modules = new ArrayList<>();

    public static UnlimitedAdmin getInstance() {
        return INSTANCE;
    }


    @Override
    public void onEnable() {
        INSTANCE = this;
        CustomCommandsManager.init(this);
        UnlimitedAdminConfig.load();
        LangConfig.load();
        Bukkit.getPluginManager().callEvent(new ExternalModuleInitEvent());
        ModulesManager.loadModules();
    }


    @Override
    public void onDisable() {
        ModulesManager.unloadModules();
    }


    public List<IModule> getModules() {
        return modules;
    }

    @NotNull
    public PlayersMapModule getPlayersMapModule() {
        return (PlayersMapModule) Objects.requireNonNull(ModulesManager.getModule(ModulesManager.PLAYERS_MAP));
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

    public ModuleConfig getModuleConfig(IModule module) {
        return new ModuleConfig(getModuleConfigFile(module));
    }

    public static class UnlimitedAdminCommand implements ICommandGroup {
        private final ArrayList<ICommand> commands = new ArrayList<>();
        private boolean isRegistered = false;

        @Override
        public @NotNull String getName() {
            return "unlimitedadmin";
        }

        @Override
        public @Nullable List<String> getAliases() {
            return Arrays.asList("una", "unlimadmin");
        }

        public void addCommand(ICommand command) {
            register();
            if (commands.stream().anyMatch(c -> c.getName().equalsIgnoreCase(command.getName()))) {
                throw new IllegalArgumentException("Command with name " + command.getName() + " already exists!");
            }
            commands.add(command);
        }

        public void removeCommand(ICommand command) {
            commands.remove(command);
            if (commands.isEmpty()) {
                unregister();
            }
        }

        public void register() {
            if (isRegistered) {
                return;
            }
            isRegistered = true;
            CustomCommandsManager.getInstance().registerCommand(this);
            setCommandExecutor(INSTANCE, new CommandExecutor(INSTANCE, this));
        }

        public void unregister() {
            if (!isRegistered) {
                return;
            }
            commands.clear();
            isRegistered = false;
        }

        @Override
        public @NotNull Collection<ICommand> getCommands() {
            return commands;
        }
    }
}
