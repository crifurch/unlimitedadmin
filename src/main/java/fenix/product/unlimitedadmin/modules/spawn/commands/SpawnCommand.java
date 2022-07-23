package fenix.product.unlimitedadmin.modules.spawn.commands;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.modules.core.AdditionalPermissions;
import fenix.product.unlimitedadmin.modules.spawn.SpawnModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class SpawnCommand implements ICommand {
    final SpawnModule module;

    public SpawnCommand(SpawnModule module) {
        this.module = module;
    }

    @Override
    public @NotNull String getName() {
        return "spawn";
    }

    @Override
    public byte getMaxArgsSize() {
        return 2;
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        String spawnName = GlobalConstants.defaultEntryName;
        UUID playerUuid;
        if (argsString.size() > 0) {
            spawnName = argsString.get(0);
        }
        final PermissionsProvider instance = PermissionsProvider.getInstance();
        if (argsString.size() > 1) {
            if (instance.havePermissionOrOp(sender, AdditionalPermissions.OTHER.getPermissionForCommand(this)).isDeniedOrUnset()) {
                sender.sendMessage("You can't teleport other player");
                return true;
            }
            UUID player = UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerUUID(argsString.get(1));
            if (player == null) {
                sender.sendMessage("No such player found");
                return true;
            }
            playerUuid = player;
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage("only player can teleport to spawn");
                return true;
            }
            playerUuid = ((Player) sender).getUniqueId();
        }
        if (instance.havePermissionOrOp(sender, getCommandPermission() + "." + spawnName).isPermittedOrUnset()) {
            if (!module.teleportPlayerToSpawn(playerUuid, spawnName)) {
                sender.sendMessage("No spawn set" + (spawnName.equals(GlobalConstants.defaultEntryName) ? "" : ":" + spawnName));
            }
        } else {
            sender.sendMessage("only player can teleport to spawn");
        }
        return true;
    }
}
