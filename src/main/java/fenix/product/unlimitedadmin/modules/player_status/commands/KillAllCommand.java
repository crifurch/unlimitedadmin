package fenix.product.unlimitedadmin.modules.player_status.commands;

import fenix.product.unlimitedadmin.api.exceptions.command.CommandErrorException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandOtherPermissionsException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class KillAllCommand implements ICommand {
    @Override
    public @NotNull String getName() {
        return "killall";
    }

    @Override
    public byte getMaxArgsSize() {
        return 0;
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws CommandOtherPermissionsException, CommandErrorException {
        final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayers) {
            if (!PlayerUtils.setHealth(player.getUniqueId(), 0)) {
                throw new CommandErrorException();
            }
        }
    }
}