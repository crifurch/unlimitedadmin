package fenix.product.unlimitedadmin.modules.player_status.commands;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandErrorException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandOnlyForUserException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandOtherPermissionsException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import fenix.product.unlimitedadmin.api.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GetPosCommand implements ICommand {
    @Override
    public @NotNull String getName() {
        return "getpos";
    }

    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws CommandOnlyForUserException, CommandOtherPermissionsException, CommandErrorException {
        UUID targetPlayer = null;
        if (args.isNotEmpty()) {
            assertOtherPermission(sender);
            targetPlayer = UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerUUID(args.get(0));
            if (targetPlayer == null) {
                throw new CommandErrorException(LangConfig.NO_SUCH_PLAYER.getText());
            }
        }
        if (targetPlayer == null) {
            assertSenderIsPlayer(sender);
            targetPlayer = ((Player) sender).getUniqueId();
        }
        final Location location = PlayerUtils.getLocation(targetPlayer);
        if (location == null) {
            throw new CommandErrorException();
        } else {
            sender.sendMessage((int) location.getX() + " " + (int) location.getY() + " " + (int) location.getZ() + " " + location.getWorld().getName());
        }
    }
}
