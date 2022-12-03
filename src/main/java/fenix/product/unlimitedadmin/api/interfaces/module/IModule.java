package fenix.product.unlimitedadmin.api.interfaces.module;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public interface IModule {
    @NotNull
    String getName();

    @NotNull
    default Collection<ICommand> getCommands() {
        return Collections.emptyList();
    }

    default Collection<Listener> getListeners() {
        return Collections.emptyList();
    }

    /**
     * only for modules that extends ICommand
     **/
    default boolean addAsCommandIfCommandsEmpty() {
        return false;
    }
}
