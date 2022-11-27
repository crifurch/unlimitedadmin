package fenix.product.unlimitedadmin.modules.chat.interfaces;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;


public interface ISubhandlerChannel extends IChatChanel {

    default String onSubhandlerMessage(@Nullable Entity sender, IChatChanel parent, String message) {
        return broadcast(sender, parent.formatMessage(sender, message), null);
    }
}
