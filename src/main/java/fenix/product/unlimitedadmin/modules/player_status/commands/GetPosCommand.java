package fenix.product.unlimitedadmin.modules.player_status.commands;

import fenix.product.unlimitedadmin.LangConfig;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.modules.core.AdditionalPermissions;
import fenix.product.unlimitedadmin.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class GetPosCommand implements ICommand {
    @Override
    public @NotNull String getName() {
        return "getpos";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        UUID targetPlayer = null;
        if (argsString.size() > 0) {
            targetPlayer = UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerUUID(argsString.get(0));
            if (targetPlayer == null) {
                sender.sendMessage(LangConfig.NO_SUCH_PLAYER.getText());
                return true;
            } else if (PermissionsProvider.getInstance().havePermissionOrOp(sender,
                    AdditionalPermissions.OTHER.getPermissionForCommand(this)) != PermissionStatus.PERMISSION_TRUE) {
                sender.sendMessage(LangConfig.NO_PERMISSIONS_USE_ON_OTHER.getText());
                return true;
            }
        }
        if (targetPlayer == null) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(LangConfig.ONLY_FOR_PLAYER_COMMAND.getText());
                return true;
            }
            targetPlayer = ((Player) sender).getUniqueId();
        }
        final Location location = PlayerUtils.getLocation(targetPlayer);
        if (location == null) {
            sender.sendMessage(LangConfig.ERROR_WHILE_COMMAND.getText());
        } else {
            sender.sendMessage(location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getWorld().getEnvironment().name());
        }

        return true;
    }
}
