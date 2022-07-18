package fenix.product.unlimitedadmin.modules.core;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import org.jetbrains.annotations.NotNull;

public abstract class BaseAdminCommand implements ICommand {
    @Override
    public @NotNull String getName() {
        return "unadmin";
    }
}
