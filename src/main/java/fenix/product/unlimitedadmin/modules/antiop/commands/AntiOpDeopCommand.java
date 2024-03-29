package fenix.product.unlimitedadmin.modules.antiop.commands;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.providers.ServerDataProvider;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AntiOpDeopCommand implements ICommand {
    @Override
    public @NotNull String getName() {
        return "deop";
    }

    @Override
    public String getUsageText() {
        return "/deop <player>";
    }

    @Override
    public byte getMinArgsSize() {
        return 1;
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        return new ArrayList<>(ServerDataProvider.getAllOPs());
    }

    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws NotifibleException {
        for (String player : args) {
            ServerDataProvider.setOP(player, false);
        }
    }

}

