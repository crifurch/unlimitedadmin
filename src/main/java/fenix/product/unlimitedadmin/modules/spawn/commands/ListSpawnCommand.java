package fenix.product.unlimitedadmin.modules.spawn.commands;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CollectionUtils;
import fenix.product.unlimitedadmin.modules.spawn.SpawnModule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ListSpawnCommand implements ICommand {
    final SpawnModule module;

    public ListSpawnCommand(SpawnModule module) {
        this.module = module;
    }

    @Override
    public byte getMaxArgsSize() {
        return 0;
    }

    @Override
    public @NotNull String getName() {
        return "spawns";
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) {
        final Set<String> spawns = module.getSpawns();
        final String join = CollectionUtils.join(Arrays.asList(spawns.toArray()), "\n");
        sender.sendMessage(join);
    }
}

