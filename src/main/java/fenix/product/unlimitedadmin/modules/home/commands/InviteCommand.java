package fenix.product.unlimitedadmin.modules.home.commands;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.GlobalConstants;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandErrorException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandNotEnoughArgsException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandOnlyForUserException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import fenix.product.unlimitedadmin.modules.home.HomeModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class InviteCommand implements ICommand {
    final HomeModule module;

    public InviteCommand(HomeModule module) {
        this.module = module;
    }

    @Override
    public String getUsageText() {
        return getName() + " <player_name> [home name]";
    }

    @Override
    public byte getMaxArgsSize() {
        return 1;
    }

    @Override
    public byte getMinArgsSize() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "homeinvite";
    }

    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws CommandOnlyForUserException,
            CommandNotEnoughArgsException,
            CommandErrorException {
        assertSenderIsPlayer(sender);
        String name = GlobalConstants.defaultEntryName;
        UUID playerToAdd = args.getPlayerUUID(0);
        if (playerToAdd == null) {
            throw new CommandErrorException(LangConfig.NO_SUCH_PLAYER.getText());
        }
        if (args.count() > 1) {
            name = args.get(1);
        }
        if (module.addParticipant(playerToAdd, ((Player) sender).getUniqueId(), name)) {
            sender.sendMessage("Player successful invited");
            String finalName = name;
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (playerToAdd.equals(player.getUniqueId())) {
                    player.sendMessage("You invited to " +
                            UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerName(((Player) sender).getUniqueId()) +
                            "(" + finalName + ") home");
                }
                    }
            );
        } else {
            sender.sendMessage("Player not invited");
        }
    }
}
