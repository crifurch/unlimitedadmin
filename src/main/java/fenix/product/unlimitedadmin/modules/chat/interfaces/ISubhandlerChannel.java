package fenix.product.unlimitedadmin.modules.chat.interfaces;

import fenix.product.unlimitedadmin.modules.chat.data.sender.ChatMessageSender;
import org.jetbrains.annotations.NotNull;


public interface ISubhandlerChannel extends IChatChanel {

    default void onSubhandlerMessage(@NotNull ChatMessageSender sender, IChatChanel parent, String message) {
        broadcast(sender, parent.formatMessage(sender, message), null);
    }
}
