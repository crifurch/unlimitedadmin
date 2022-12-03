package fenix.product.unlimitedadmin.modules.maintain;

import fenix.product.unlimitedadmin.ModulesManager;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.managers.ServerDataManager;
import fenix.product.unlimitedadmin.api.modules.AdminModule;
import fenix.product.unlimitedadmin.api.utils.PlayerUtils;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.modules.maintain.commands.LockWorldCommand;
import fenix.product.unlimitedadmin.modules.maintain.commands.MaintainModeCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class MaintainModule extends AdminModule implements Listener {
    private static final String bypassPermission = "unlimitedadmin.maintain.bypass";

    private final List<ICommand> commands = new ArrayList<>();
    final UnlimitedAdmin plugin;

    public MaintainModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return ModulesManager.MAINTAIN.getName();
    }

    @Override
    public void onEnable() {
        MaintainModuleConfig.load();
        commands.add(new MaintainModeCommand(this));
        commands.add(new LockWorldCommand(this));
    }

    @Override
    public void onDisable() {

    }

    @Override
    public @NotNull List<ICommand> getCommands() {
        return commands;
    }

    @Override
    public Collection<Listener> getListeners() {
        return Collections.singletonList(this);
    }

    public void setMaintainMode(boolean mode) {
        if (mode == MaintainModuleConfig.IN_MAINTAIN.getBoolean()) {
            return;
        }
        MaintainModuleConfig.IN_MAINTAIN.set(mode, true);
        if (!mode) {
            return;
        }
        final ArrayList<? extends Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        players.forEach(player -> onPlayerLogin(new PlayerJoinEvent(player, "")));
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerJoinEvent event) {
        if (!MaintainModuleConfig.IN_MAINTAIN.getBoolean()) {
            return;
        }
        if (event.getPlayer().isOp() ||
                PermissionsProvider.getInstance().havePermission(event.getPlayer(), bypassPermission) == PermissionStatus.PERMISSION_TRUE) {
            return;
        }
        event.getPlayer().kickPlayer(LangConfig.SERVER_IN_MAINTAIN_MODE.getText());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        final List<World> lockedWorlds = getLockedWorlds();
        if (lockedWorlds.isEmpty()) {
            return;
        }
        if (lockedWorlds.contains(event.getPlayer().getWorld())) {
            if (event.getPlayer().isOp() ||
                    PermissionsProvider.getInstance().havePermission(event.getPlayer(), bypassPermission) == PermissionStatus.PERMISSION_TRUE) {
                return;
            }
            final World world = Bukkit.getWorld(ServerDataManager.getMainWorldName());
            if (world == null || lockedWorlds.contains(world)) {
                event.getPlayer().kickPlayer(LangConfig.WORLD_LOCKED_FOR_ENTERING.getText());
                return;
            }
            PlayerUtils.setLocation(event.getPlayer().getUniqueId(), world.getSpawnLocation());
            event.getPlayer().sendMessage(LangConfig.WORLD_LOCKED_FOR_ENTERING.getText());
        }
    }

    public void setLockedWorld(World world, boolean locked) {
        final List<String> stringList = MaintainModuleConfig.LOCKED_WORLDS.getStringList();
        if (locked) {
            stringList.add(world.getName());
        } else {
            stringList.remove(world.getName());
        }
        MaintainModuleConfig.LOCKED_WORLDS.set(stringList, true);
    }

    public void toggleLockedWorld(World world) {
        final List<String> stringList = MaintainModuleConfig.LOCKED_WORLDS.getStringList();
        if (stringList.contains(world.getName())) {
            stringList.remove(world.getName());
        } else {
            stringList.add(world.getName());
        }
        MaintainModuleConfig.LOCKED_WORLDS.set(stringList, true);
    }

    public boolean isLockedWorld(World world) {
        return MaintainModuleConfig.LOCKED_WORLDS.getStringList().contains(world.getName());
    }

    public List<World> getLockedWorlds() {
        return MaintainModuleConfig.LOCKED_WORLDS.getStringList().stream().map(Bukkit::getWorld)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}