package fenix.product.unlimitedadmin.modules.player_status.commands;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandErrorException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandOnlyForUserException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandOtherPermissionsException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class HealCommand implements ICommand {
    @Override
    public @NotNull String getName() {
        return "heal";
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws CommandOtherPermissionsException, CommandErrorException, CommandOnlyForUserException {
        UUID targetPlayer = null;
        if (argsString.size() > 0) {
            assertOtherPermission(sender);
            targetPlayer = UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerUUID(argsString.get(0));
            if (targetPlayer == null) {
                throw new CommandErrorException(LangConfig.NO_SUCH_PLAYER.getText());
            }
        }
        if (targetPlayer == null) {
            assertSenderIsPlayer(sender);
            targetPlayer = ((Player) sender).getUniqueId();
        }

        if (!PlayerUtils.setHealth(targetPlayer, PlayerUtils.getMaxHealth(targetPlayer, true))) {
            throw new CommandErrorException();
        }
    }
}
