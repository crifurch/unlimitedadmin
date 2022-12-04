package fenix.product.unlimitedadmin.modules.player_status.commands;

import fenix.product.unlimitedadmin.api.exceptions.command.CommandErrorException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import fenix.product.unlimitedadmin.api.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class FeedAllCommand implements ICommand {
    @Override
    public @NotNull String getName() {
        return "feedall";
    }

    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws CommandErrorException {
        final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayers) {
            if (!PlayerUtils.setFood(player.getUniqueId(), 100)) {
                throw new CommandErrorException();
            }
        }
    }
}
