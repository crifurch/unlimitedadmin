package fenix.product.unlimitedadmin.modules.player_status;

import fenix.product.unlimitedadmin.ModulesManager;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.modules.RawModule;
import fenix.product.unlimitedadmin.modules.player_status.commands.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatusModule extends RawModule {

    private final List<ICommand> commands = new ArrayList<>();
    final UnlimitedAdmin plugin;

    public PlayerStatusModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;

    }

    @Override
    public @NotNull String getName() {
        return ModulesManager.PLAYER_STATUS.getName();
    }

    @Override
    public void onEnable() {
        commands.add(new HealCommand());
        commands.add(new HealAllCommand());
        commands.add(new KillCommand());
        commands.add(new KillAllCommand());
        commands.add(new FeedCommand());
        commands.add(new FeedAllCommand());
        commands.add(new FlyCommand());
        commands.add(new GetPosCommand());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public @NotNull List<ICommand> getCommands() {
        return commands;
    }


}