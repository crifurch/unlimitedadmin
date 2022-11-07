package fenix.product.unlimitedadmin.modules.spawn.commands;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.LangConfig;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.modules.core.AdditionalPermissions;
import fenix.product.unlimitedadmin.modules.spawn.SpawnModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SpawnCommand implements ICommand {
    final SpawnModule module;

    public SpawnCommand(SpawnModule module) {
        this.module = module;
    }

    @Override
    public String getUsageText() {
        return ICommand.super.getUsageText() + "[spawn] [player]";
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
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        final PermissionsProvider instance = PermissionsProvider.getInstance();
        if (i == 0) {
            final List<String> spawns = new ArrayList<>(module.getSpawns());
            spawns.removeIf(s -> instance.havePermissionOrOp(sender, getCommandPermission() + "." + s).isPermittedOrUnset());
            return spawns;
        }
        if (i == 1 && instance.havePermissionOrOp(sender, AdditionalPermissions.OTHER.getPermissionForCommand(this)) == PermissionStatus.PERMISSION_TRUE) {
            return null;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        String spawnName = GlobalConstants.defaultEntryName;
        UUID playerUuid = null;
        if (argsString.size() > 0) {
            spawnName = argsString.get(0);
        }
        final PermissionsProvider instance = PermissionsProvider.getInstance();
        if (argsString.size() > 1) {
            if (instance.havePermissionOrOp(sender, AdditionalPermissions.OTHER.getPermissionForCommand(this)).isDeniedOrUnset()) {
                sender.sendMessage(LangConfig.NO_PERMISSIONS_USE_ON_OTHER.getText());
                return true;
            }
            UUID player = UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerUUID(argsString.get(1));
            if (player == null) {
                sender.sendMessage(LangConfig.NO_SUCH_PLAYER.getText());
                return true;
            }
            playerUuid = player;
        }

        if (playerUuid == null) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(LangConfig.ONLY_FOR_PLAYER_COMMAND.getText());
                return true;
            }
            playerUuid = ((Player) sender).getUniqueId();
        }


        if (instance.havePermissionOrOp(sender, getCommandPermission() + "." + spawnName).isPermittedOrUnset()) {
            if (!module.teleportPlayerToSpawn(playerUuid, spawnName)) {
                sender.sendMessage("No spawn set" + (spawnName.equals(GlobalConstants.defaultEntryName) ? "" : ":" + spawnName));
            }
        } else {
            sender.sendMessage("You can not teleport");
        }
        return true;
    }
}
