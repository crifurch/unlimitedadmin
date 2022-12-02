package fenix.product.unlimitedadmin.modules.teleporting;

import fenix.product.unlimitedadmin.ModulesManager;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.module.IModule;
import fenix.product.unlimitedadmin.modules.teleporting.commands.TpCommand;
import fenix.product.unlimitedadmin.modules.teleporting.commands.TpPosCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TeleportingModule implements IModule {

    private final List<ICommand> commands;
    final UnlimitedAdmin plugin;

    public TeleportingModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;
        commands = Arrays.asList(
                new TpCommand(plugin),
                new TpPosCommand(plugin)
        );
    }

    @Override
    public @NotNull String getName() {
        return ModulesManager.TELEPORT.getName();
    }

    @Override
    public List<ICommand> getCommands() {
        return commands;
    }
}
