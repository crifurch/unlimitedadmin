package fenix.product.unlimitedadmin.modules.teleporting.commands;

import fenix.product.unlimitedadmin.LangConfig;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.modules.core.AdditionalPermissions;
import fenix.product.unlimitedadmin.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class TpCommand implements ICommand {
    private final UnlimitedAdmin plugin;


    public TpCommand(UnlimitedAdmin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getUsageText() {
        return ICommand.super.getUsageText() + " <player> [player_to]";
    }

    @Override
    public byte getMaxArgsSize() {
        return 2;
    }

    @Override
    public @NotNull String getName() {
        return "tp";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        UUID targetPlayer = null;
        UUID tpTo;
        if (argsString.size() < 1) {
            sender.sendMessage(getUsageText());
            return true;
        }
        if (argsString.size() > 1) {
            tpTo = plugin.getPlayersMapModule().getPlayerUUID(argsString.get(1));
            if (tpTo == null) {
                sender.sendMessage(LangConfig.NO_SUCH_PLAYER.getText());
                return true;
            }
            targetPlayer = plugin.getPlayersMapModule().getPlayerUUID(argsString.get(0));
            if (targetPlayer == null) {
                sender.sendMessage(LangConfig.NO_SUCH_PLAYER.getText());
                return true;
            } else if (PermissionsProvider.getInstance().havePermissionOrOp(sender,
                    AdditionalPermissions.OTHER.getPermissionForCommand(this)) != PermissionStatus.PERMISSION_TRUE) {
                sender.sendMessage(LangConfig.NO_PERMISSIONS_USE_ON_OTHER.getText());
                return true;
            }
        } else {
            tpTo = plugin.getPlayersMapModule().getPlayerUUID(argsString.get(0));
            if (tpTo == null) {
                sender.sendMessage(LangConfig.NO_SUCH_PLAYER.getText());
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

        if (!PlayerUtils.setLocation(targetPlayer, PlayerUtils.getLocation(tpTo))) {
            sender.sendMessage(LangConfig.ERROR_WHILE_COMMAND.getText());
        }

        return true;
    }
}
