package fenix.product.unlimitedadmin.modules.home.commands;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.LangConfig;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.home.HomeModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
    public @NotNull String getName() {
        return "homeinvite";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LangConfig.ONLY_FOR_PLAYER_COMMAND.getText());
            return true;
        }
        if (argsString.size() < 1) {
            sender.sendMessage(getUsageText());
            return true;
        }
        String name = GlobalConstants.defaultEntryName;
        UUID playerToAdd = UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerUUID(argsString.get(0));
        if (playerToAdd == null) {
            sender.sendMessage(LangConfig.NO_SUCH_PLAYER.getText());
            return true;
        }
        if (argsString.size() > 1) {
            name = argsString.get(1);
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
        return true;
    }
}
