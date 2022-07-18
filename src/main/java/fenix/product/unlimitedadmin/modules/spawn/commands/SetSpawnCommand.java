package fenix.product.unlimitedadmin.modules.spawn.commands;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.spawn.SpawnModule;
import fenix.product.unlimitedadmin.modules.spawn.SpawnModuleConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class SetSpawnCommand implements ICommand {
    final SpawnModule module;

    public SetSpawnCommand(SpawnModule module) {
        this.module = module;
    }

    @Override
    public @NotNull String getName() {
        return "setspawn";
    }

    @Override
    public byte getMaxArgsSize() {
        return 1;
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("only player can own this command");
            return true;
        }
        String name = GlobalConstants.defaultEntryName;
        if (argsString.size() > 0) {
            name = argsString.get(0);
        }
        int allowedSpawns = SpawnModuleConfig.SPAWN_LIMIT.getInt();
        if (allowedSpawns < 0) {
            allowedSpawns = Integer.MAX_VALUE;
        }
        if (allowedSpawns == 0) {
            sender.sendMessage("server disable spawns");
            return true;
        }
        final Set<String> spawns = module.getSpawns();
        if (!spawns.contains(name)) {
            if (spawns.size() >= allowedSpawns) {
                sender.sendMessage("no more spawn can be created");
                return true;
            }
        }
        module.setSpawn(name, ((Player) sender).getLocation());
        return true;
    }
}
