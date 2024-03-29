package fenix.product.unlimitedadmin.modules.antiop.commands;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.providers.ServerDataProvider;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import fenix.product.unlimitedadmin.modules.antiop.AntiOPConfig;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AntiOpOpCommand implements ICommand {
    @Override
    public @NotNull String getName() {
        return "op";
    }

    @Override
    public String getUsageText() {
        return "/op <player>";
    }

    @Override
    public byte getMinArgsSize() {
        return 1;
    }


    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws NotifibleException {
        final List<String> ops = new ArrayList<>(AntiOPConfig.OP_LIST.getStringList());
        ArrayList<String> notAdded = new ArrayList<>();
        for (String player : args) {
            if (!ops.contains(player)) {
                notAdded.add(player);
                continue;
            }
            ServerDataProvider.setOP(player, true);
        }
        if (!notAdded.isEmpty()) {
            throw new NotifibleException("Players " + String.join(", ", notAdded) + " can't be OP");
        }
    }

}
