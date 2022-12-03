package fenix.product.unlimitedadmin.modules.teleporting;

import fenix.product.unlimitedadmin.ModulesManager;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.module.IModuleDefinition;
import fenix.product.unlimitedadmin.api.modules.RawModule;
import fenix.product.unlimitedadmin.modules.teleporting.commands.TpCommand;
import fenix.product.unlimitedadmin.modules.teleporting.commands.TpPosCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TeleportingModule extends RawModule {

    private final ArrayList<ICommand> commands = new ArrayList<>();
    final UnlimitedAdmin plugin;

    public TeleportingModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;

    }

    @Override
    public @NotNull IModuleDefinition getDefinition() {
        return ModulesManager.TELEPORT;
    }


    @Override
    public void onEnable() {
        commands.add(new TpCommand(plugin));
        commands.add(new TpPosCommand(plugin));
    }

    @Override
    public void onDisable() {

    }

    @Override
    public @NotNull List<ICommand> getCommands() {
        return commands;
    }
}
