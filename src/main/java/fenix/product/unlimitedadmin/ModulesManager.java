package fenix.product.unlimitedadmin;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.module.IModule;
import fenix.product.unlimitedadmin.api.interfaces.module.IModuleDefinition;
import fenix.product.unlimitedadmin.api.managers.CustomCommandsManager;
import fenix.product.unlimitedadmin.api.utils.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Supplier;

public enum ModulesManager implements IModuleDefinition {
    PLAYERS_MAP("players_map", "*.playersmap.PlayersMapModule", ModuleType.admin, () -> true),
    WORLDS("worlds", "*.world.WorldsModule", ModuleType.admin, UnlimitedAdminConfig.WORLDS_MODULE_ENABLED::getBoolean),
    MAINTAIN("maintain", "*.maintain.MaintainModule", ModuleType.admin, UnlimitedAdminConfig.MAINTAIN_MODULE_ENABLED::getBoolean),
    TABLIST("tablist", "*.tablist.TabListModule", ModuleType.admin, UnlimitedAdminConfig.TABLIST_MODULE_ENABLED::getBoolean),
    TELEPORT("teleport", "*.teleporting.TeleportingModule", ModuleType.raw, UnlimitedAdminConfig.TELEPORT_MODULE_ENABLED::getBoolean),
    SPAWN("spawn", "*.spawn.SpawnModule", ModuleType.raw, UnlimitedAdminConfig.SPAWN_MODULE_ENABLED::getBoolean),
    HOME("home", "*.home.HomeModule", ModuleType.raw, UnlimitedAdminConfig.HOME_MODULE_ENABLED::getBoolean),
    PLAYER_STATUS("player_status", "*.player_status.PlayerStatusModule", ModuleType.raw, UnlimitedAdminConfig.PLAYER_STATUS_MODULE_ENABLED::getBoolean),
    SHOP("shop", "*.shop.ShopModule", ModuleType.raw, UnlimitedAdminConfig.SHOP_MODULE_ENABLED::getBoolean),
    CHAT("chat", "*.chat.ChatModule", ModuleType.raw, UnlimitedAdminConfig.CHAT_MODULE_ENABLED::getBoolean),
    ANTIOP("antiop", "*.antiop.AntiOPModule", ModuleType.raw, UnlimitedAdminConfig.ANTIOP_MODULE_ENABLED::getBoolean),

    ;
    private static final Collection<IModuleDefinition> externalValues = new ArrayList<>();
    private static final Map<IModuleDefinition, IModule> loadedModules = new HashMap<>();
    private static final UnlimitedAdmin.UnlimitedAdminCommand command = new UnlimitedAdmin.UnlimitedAdminCommand();
    final String name;
    final String classPath;

    final ModuleType type;
    final Supplier<@NotNull Boolean> enabled;

    ModulesManager(@NotNull String name, @NotNull String classPath,
                   @NotNull ModuleType type, @NotNull Supplier<@NotNull Boolean> enabled) {
        this.name = name;
        this.classPath = classPath;
        this.enabled = enabled;
        this.type = type;
    }

    public static void addExternalModule(IModuleDefinition module) {
        if (!loadedModules.isEmpty()) {
            throw new IllegalStateException("You can't add external modules after loading");
        }
        if (externalValues.stream().anyMatch(m -> m.getName().equals(module.getName()))) {
            throw new IllegalArgumentException("Module with name " + module.getName() + " already exists");
        }
        externalValues.add(module);
    }

    @Nullable
    public static IModule getModule(IModuleDefinition module) {
        return loadedModules.get(module);
    }

    public static void loadModules() {
        if (!loadedModules.isEmpty()) {
            throw new IllegalStateException("Modules already loaded, use " + ModulesManager.class.getSimpleName() + ".reloadModules() instead");
        }
        final Collection<IModuleDefinition> modules = new ArrayList<>(values().length + externalValues.size());
        modules.addAll(Arrays.asList(values()));
        modules.addAll(externalValues);
        for (IModuleDefinition module : modules) {
            if (!module.isEnabled()) {
                continue;
            }
            try {
                enableModule(module);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Object tryGetModuleConstructor(Class<?> clazz) {
        final Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();

        try {
            final Class<?>[] parameterTypes = declaredConstructors[0].getParameterTypes();
            if (parameterTypes.length == 0) {
                return declaredConstructors[0].newInstance();
            } else {
                return declaredConstructors[0].newInstance(JavaPlugin.getPlugin(UnlimitedAdmin.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
        return null;
    }

    @Nullable
    private static IModule loadModule(IModuleDefinition module) {
        if (loadedModules.containsKey(module)) {
            throw new IllegalStateException("Module " + module.getName() + " already loaded");
        }
        String classPath = module.getClassPath();
        if (module.getClassPath().startsWith("*")) {
            classPath = "fenix.product.unlimitedadmin.modules" + classPath.substring(1);
        }
        try {
            Class<?> clazz = Class.forName(classPath);
            Object instance = tryGetModuleConstructor(clazz);
            if (instance == null) {
                throw new IllegalStateException("Module " + classPath + " doesn't have a valid constructor");
            }
            if (!(instance instanceof IModule)) {
                throw new IllegalStateException("Class " + classPath + " is not instance of " + IModule.class.getSimpleName());
            }
            IModule moduleInstance = (IModule) instance;
            loadedModules.put(module, moduleInstance);
            return moduleInstance;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void enableModule(IModuleDefinition module) {
        if (loadedModules.containsKey(module)) {
            throw new IllegalStateException("Module " + module.getName() + " already loaded");
        }
        final IModule iModule = loadModule(module);
        if (iModule != null) {
            switch (module.getType()) {
                case raw:
                    for (ICommand command : iModule.getCommands()) {
                        final PluginCommand pluginCommand = CustomCommandsManager.getInstance().registerCommand(command);
                        pluginCommand.setExecutor(new CommandExecutor(JavaPlugin.getPlugin(UnlimitedAdmin.class), command));
                    }
                    break;
                case admin:
                    command.addCommand(iModule);
                    break;
            }
        }
    }

    public static void unloadModules() {
        loadedModules.clear();
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getClassPath() {
        return classPath;
    }

    @Override
    public @NotNull ModuleType getType() {
        return type;
    }

    @Override
    public boolean isEnabled() {
        return enabled.get();
    }

    @Override
    public @Nullable IModule getModule() {
        return loadedModules.get(this);
    }


}
