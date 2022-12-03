package fenix.product.unlimitedadmin.api.interfaces;

public interface IReloadable {
    default void reload() {
        onDisable();
        onEnable();
    }

    void onEnable();

    void onDisable();
}
