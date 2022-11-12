package fenix.product.unlimitedadmin.modules.spawn.commands;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandPermissionException;
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
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        assertSenderIsPlayer(sender);
        String name = GlobalConstants.defaultEntryName;
        if (argsString.size() > 0) {
            name = argsString.get(0);
        }
        int allowedSpawns = SpawnModuleConfig.SPAWN_LIMIT.getInt();
        if (allowedSpawns < 0) {
            allowedSpawns = Integer.MAX_VALUE;
        }
        if (allowedSpawns == 0) {
            throw new CommandPermissionException();
        }
        final Set<String> spawns = module.getSpawns();
        if (!spawns.contains(name)) {
            if (spawns.size() >= allowedSpawns) {
                throw new CommandPermissionException();
            }
        }
        module.setSpawn(name, ((Player) sender).getLocation());
        sender.sendMessage(LangConfig.SPAWN_CREATED.getText(name));
    }
}
