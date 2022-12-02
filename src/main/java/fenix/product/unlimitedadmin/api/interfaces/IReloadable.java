package fenix.product.unlimitedadmin.api.interfaces;

import fenix.product.unlimitedadmin.api.interfaces.module.IEnableable;

public interface IReloadable extends IEnableable {
    default void reload() {
        onDisable();
        onEnable();
    }
}
