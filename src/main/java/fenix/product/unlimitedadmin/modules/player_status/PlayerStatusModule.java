package fenix.product.unlimitedadmin.modules.player_status;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.IModule;
import fenix.product.unlimitedadmin.modules.player_status.commands.FeedCommand;
import fenix.product.unlimitedadmin.modules.player_status.commands.FlyCommand;
import fenix.product.unlimitedadmin.modules.player_status.commands.HealCommand;
import fenix.product.unlimitedadmin.modules.player_status.commands.KillCommand;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatusModule implements IModule {

    private final List<ICommand> commands = new ArrayList<>();
    final UnlimitedAdmin plugin;

    public PlayerStatusModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;
        this.commands.add(new HealCommand());
        this.commands.add(new KillCommand());
        this.commands.add(new FeedCommand());
        this.commands.add(new FlyCommand());
    }

    @Override
    public String getName() {
        return "playerstatus";
    }

    @Override
    public List<ICommand> getCommands() {
        return commands;
    }
}