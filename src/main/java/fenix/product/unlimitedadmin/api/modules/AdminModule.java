package fenix.product.unlimitedadmin.api.modules;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.ICommandGroup;
import fenix.product.unlimitedadmin.api.interfaces.module.IModule;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class AdminModule extends EnableStateProvider implements IModule, ICommandGroup {

    @Override
    public @NotNull Collection<ICommand> getCommands() {
        return IModule.super.getCommands();
    }

}