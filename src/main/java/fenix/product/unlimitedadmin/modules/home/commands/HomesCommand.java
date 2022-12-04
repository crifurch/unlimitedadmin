package fenix.product.unlimitedadmin.modules.home.commands;

import fenix.product.unlimitedadmin.api.exceptions.command.CommandNotEnoughArgsException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandOnlyForUserException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import fenix.product.unlimitedadmin.modules.home.HomeModule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HomesCommand implements ICommand {
    protected final HomeModule module;

    public HomesCommand(HomeModule module) {
        this.module = module;
    }

    @Override
    public @NotNull String getName() {
        return "homes";
    }

    @Override
    public byte getMaxArgsSize() {
        return 0;
    }

    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws CommandNotEnoughArgsException, CommandOnlyForUserException {
    }
}
