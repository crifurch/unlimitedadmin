package fenix.product.unlimitedadmin.modules.antiop.commands;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.ICommandGroup;
import fenix.product.unlimitedadmin.modules.antiop.AntiOPModule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AntiOPGroupCommand implements ICommandGroup {
    final AntiOPModule module;
    final List<ICommand> commands;

    public AntiOPGroupCommand(AntiOPModule module) {
        this.module = module;
        this.commands = Arrays.asList(
                new AntiOPAddCommand(),
                new AntiOPRemoveCommand(module),
                new AntiOPCheckCommand(module)
        );
    }

    @Override
    public @NotNull String getName() {
        return "antiop";
    }

    @Override
    public Collection<ICommand> getCommands() {
        return commands;
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        assertSenderIsPlayer(sender);
        ICommandGroup.super.onCommand(sender, argsString);
    }
}
