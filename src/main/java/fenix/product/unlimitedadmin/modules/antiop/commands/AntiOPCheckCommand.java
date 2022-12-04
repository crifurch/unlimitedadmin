package fenix.product.unlimitedadmin.modules.antiop.commands;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import fenix.product.unlimitedadmin.modules.antiop.AntiOPModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AntiOPCheckCommand implements ICommand {
    final AntiOPModule module;

    public AntiOPCheckCommand(AntiOPModule module) {
        this.module = module;
    }

    @Override
    public byte getMaxArgsSize() {
        return 0;
    }

    @Override
    public @NotNull String getName() {
        return "check";
    }

    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws NotifibleException {
        module.checkOps(Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }
}
