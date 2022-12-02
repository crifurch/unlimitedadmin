package fenix.product.unlimitedadmin.api.interfaces.module;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IModuleDefinition {
    @NotNull
    String getName();

    @NotNull
    String getClassPath();

    @NotNull
    ModuleType getType();

    boolean isEnabled();

    @Nullable
    IModule getModule();

    enum ModuleType {
        raw,
        admin,
    }
}
