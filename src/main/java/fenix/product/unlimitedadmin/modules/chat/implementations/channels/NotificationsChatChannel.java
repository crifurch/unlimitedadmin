package fenix.product.unlimitedadmin.modules.chat.implementations.channels;

import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ILoggedChat;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NotificationsChatChannel implements ILoggedChat {

    public static final String CHANNEL_PREFIX = "!notification!";

    @Override
    public String getName() {
        return "notifications";
    }

    @Override
    public String getChannelPrefix() {
        return CHANNEL_PREFIX;
    }

    @Override
    public String formatMessage(@Nullable Entity sender, @NotNull String message) {
        final String s = ILoggedChat.super.formatMessage(sender, message);
        return PlaceHolderUtils.replaceColors(s);
    }
}
