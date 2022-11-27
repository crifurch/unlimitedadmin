package fenix.product.unlimitedadmin.modules.teleporting.commands;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandErrorException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
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
    public byte getMinArgsSize() {
        return 1;
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
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        UUID targetPlayer = null;
        UUID tpTo;
        int indexTpTo = 0;
        if (argsString.size() > 1) {
            assertOtherPermission(sender);
            indexTpTo = 1;
            targetPlayer = plugin.getPlayersMapModule().getPlayerUUID(argsString.get(0));
            if (targetPlayer == null) {
                throw new CommandErrorException(LangConfig.NO_SUCH_PLAYER.getText(argsString.get(0)));
            }
        }
        tpTo = plugin.getPlayersMapModule().getPlayerUUID(argsString.get(indexTpTo));
        if (tpTo == null) {
            throw new CommandErrorException(LangConfig.NO_SUCH_PLAYER.getText(argsString.get(indexTpTo)));
        }
        if (targetPlayer == null) {
            assertSenderIsPlayer(sender);
            targetPlayer = ((Player) sender).getUniqueId();
        }

        if (!PlayerUtils.setLocation(targetPlayer, PlayerUtils.getLocation(tpTo))) {
           throw new CommandErrorException();
        }
    }
}
