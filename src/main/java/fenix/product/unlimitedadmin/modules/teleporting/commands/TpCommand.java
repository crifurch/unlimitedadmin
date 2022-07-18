package fenix.product.unlimitedadmin.modules.teleporting.commands;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TpCommand implements ICommand {
    private final UnlimitedAdmin plugin;


    public TpCommand(UnlimitedAdmin plugin) {
        this.plugin = plugin;
    }


    @Override
    public @NotNull String getName() {
        return "tp";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {

        return false;
    }
}
