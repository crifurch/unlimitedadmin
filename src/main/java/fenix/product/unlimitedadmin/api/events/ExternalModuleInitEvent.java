package fenix.product.unlimitedadmin.api.events;

import fenix.product.unlimitedadmin.ModulesManager;
import fenix.product.unlimitedadmin.api.interfaces.module.IModuleDefinition;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ExternalModuleInitEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public void addModules(IModuleDefinition... module) {
        for (IModuleDefinition modulesManager : module) {
            ModulesManager.addExternalModule(modulesManager);
        }
    }
}
