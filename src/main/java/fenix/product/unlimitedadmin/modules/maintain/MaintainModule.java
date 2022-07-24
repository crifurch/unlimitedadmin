package fenix.product.unlimitedadmin.modules.maintain;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.IModule;

import java.util.ArrayList;
import java.util.List;

public class MaintainModule implements IModule {

    private final List<ICommand> commands = new ArrayList<>();
    final UnlimitedAdmin plugin;

    public MaintainModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "maintain";
    }

    @Override
    public List<ICommand> getCommands() {
        return commands;
    }
}