package fenix.product.unlimitedadmin.modules.player_status;

import fenix.product.unlimitedadmin.ModulesManager;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.module.IModule;
import fenix.product.unlimitedadmin.modules.player_status.commands.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatusModule implements IModule {

    private final List<ICommand> commands = new ArrayList<>();
    final UnlimitedAdmin plugin;

    public PlayerStatusModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;
        this.commands.add(new HealCommand());
        this.commands.add(new HealAllCommand());
        this.commands.add(new KillCommand());
        this.commands.add(new KillAllCommand());
        this.commands.add(new FeedCommand());
        this.commands.add(new FeedAllCommand());
        this.commands.add(new FlyCommand());
        this.commands.add(new GetPosCommand());
    }

    @Override
    public @NotNull String getName() {
        return ModulesManager.PLAYER_STATUS.getName();
    }

    @Override
    public @NotNull List<ICommand> getCommands() {
        return commands;
    }
}