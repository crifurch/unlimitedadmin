package fenix.product.unlimitedadmin.modules.spawn.commands;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.spawn.SpawnModule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DelSpawnCommand implements ICommand {
    final SpawnModule module;

    public DelSpawnCommand(SpawnModule module) {
        this.module = module;
    }

    @Override
    public @NotNull String getName() {
        return "delspawn";
    }

    @Override
    public byte getMaxArgsSize() {
        return 1;
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, int i) {
        return new ArrayList<>(module.getSpawns());
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        String name = GlobalConstants.defaultEntryName;
        if (argsString.size() > 0) {
            name = argsString.get(0);
        }
        final Set<String> spawns = module.getSpawns();
        if (!spawns.contains(name)) {
            sender.sendMessage("no spawn with name " + name);
            return true;
        }
        module.deleteSpawn(name);
        return true;
    }
}
