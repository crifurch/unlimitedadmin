package fenix.product.unlimitedadmin.modules.player_status.commands;

import fenix.product.unlimitedadmin.LangConfig;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.modules.core.AdditionalPermissions;
import fenix.product.unlimitedadmin.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class FeedAllCommand implements ICommand {
    @Override
    public @NotNull String getName() {
        return "feedall";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        if (PermissionsProvider.getInstance().havePermissionOrOp(sender,
                AdditionalPermissions.OTHER.getPermissionForCommand(this)) != PermissionStatus.PERMISSION_TRUE) {
            sender.sendMessage(LangConfig.NO_PERMISSIONS_USE_ON_OTHER.getText());
            return true;
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!PlayerUtils.setFood(player.getUniqueId(), 100)) {
                sender.sendMessage(LangConfig.ERROR_WHILE_COMMAND.getText());
            }
        });


        return true;
    }
}
