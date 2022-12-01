package fenix.product.unlimitedadmin.modules.antiop.commands;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.antiop.AntiOPConfig;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AntiOpCommand implements ICommand {
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
    public byte getMaxArgsSize() {
        return 1;
    }


    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        final String name = argsString.get(0);
        final List<String> ops = new ArrayList<>(AntiOPConfig.OP_LIST.getStringList());
        if (!ops.contains(name)) {
           throw new RuntimeException("Player is not in the op list");
        }
    }

}
