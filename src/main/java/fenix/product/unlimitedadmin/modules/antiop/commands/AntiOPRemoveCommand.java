package fenix.product.unlimitedadmin.modules.antiop.commands;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.antiop.AntiOPConfig;
import fenix.product.unlimitedadmin.modules.antiop.AntiOPModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AntiOPRemoveCommand implements ICommand {
    final AntiOPModule module;

    public AntiOPRemoveCommand(AntiOPModule module) {
        this.module = module;
    }

    @Override
    public @NotNull String getName() {
        return "remove";
    }

    @Override
    public String getUsageText() {
        return "/remove <uuid|nickname>";
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
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        return AntiOPConfig.OP_LIST.getStringList();
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        final String name = argsString.get(0);
        final List<String> ops = new ArrayList<>(AntiOPConfig.OP_LIST.getStringList());
        if (!ops.contains(name)) {
            return;
        }
        ops.remove(name);
        AntiOPConfig.OP_LIST.set(ops, true);
        module.checkOps(Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

}