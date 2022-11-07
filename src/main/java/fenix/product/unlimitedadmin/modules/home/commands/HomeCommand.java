package fenix.product.unlimitedadmin.modules.home.commands;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.modules.home.HomeModule;
import fenix.product.unlimitedadmin.modules.home.data.Home;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HomeCommand implements ICommand {
    final HomeModule module;

    public HomeCommand(HomeModule module) {
        this.module = module;
    }

    @Override
    public List<String> getPermissions() {
        return Collections.singletonList(getCommandPermission() + ".other");
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        if (!(sender instanceof Player))
            return ICommand.super.getTabCompletion(sender, args, i);
        final UUID uniqueId = ((Player) sender).getUniqueId();
        return module.getHomes(uniqueId).stream().map(Home::getName).collect(Collectors.toList());
    }

    @Override
    public byte getMaxArgsSize() {
        return 2;
    }

    @Override
    public @NotNull String getName() {
        return "home";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        String homeName = GlobalConstants.defaultEntryName;
        UUID homePlayerUuid = null;
        if (sender instanceof Player) {
            homePlayerUuid = ((Player) sender).getUniqueId();
        }
        UUID playerUuid = null;
        if (argsString.size() > 0) {
            homeName = argsString.get(0);
        }
        if (argsString.size() > 1) {
            if (PermissionsProvider.getInstance().havePermissionOrOp(sender, getCommandPermission() + ".other") != PermissionStatus.PERMISSION_TRUE) {
                sender.sendMessage("You can't teleport other player");
                return true;
            }
            UUID player = UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerUUID(argsString.get(1));
            if (player == null) {
                sender.sendMessage("No such player found");
                return true;
            }
            playerUuid = player;
            if (homePlayerUuid == null) {
                homePlayerUuid = playerUuid;
            }
        }
        if (playerUuid == null) {
            playerUuid = homePlayerUuid;
        }
        if (playerUuid == null) {
            sender.sendMessage("only player can teleport to spawn");
            return true;
        }

        Home home = module.getHome(homePlayerUuid, homeName);
        if (home == null) {
            sender.sendMessage("No home with name " + homeName);
            return true;
        }
        if (!module.teleportPlayerToHome(playerUuid, home)) {
            sender.sendMessage("You can not teleport to this home");
        }
        return true;
    }
}
