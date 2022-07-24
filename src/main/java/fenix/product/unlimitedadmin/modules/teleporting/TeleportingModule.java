package fenix.product.unlimitedadmin.modules.teleporting;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.IModule;
import fenix.product.unlimitedadmin.modules.teleporting.commands.TpCommand;
import fenix.product.unlimitedadmin.modules.teleporting.commands.TpPosCommand;

import java.util.ArrayList;
import java.util.List;

public class TeleportingModule implements IModule {

    private List<ICommand> commands = new ArrayList<>();
    final UnlimitedAdmin plugin;

    public TeleportingModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;
        commands.add(new TpCommand(plugin));
        commands.add(new TpPosCommand(plugin));
    }

    @Override
    public String getName() {
        return "teleporting";
    }

    @Override
    public List<ICommand> getCommands() {
        return commands;
    }
}
