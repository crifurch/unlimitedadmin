package fenix.product.unlimitedadmin.modules.chat.implementations.channels;

import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ILoggedChat;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NotificationsChatChannel implements ILoggedChat {

    public static final String CHANNEL_PREFIX = "!notification!";

    final ChatModule chatModule;

    public NotificationsChatChannel(ChatModule chatModule) {
        this.chatModule = chatModule;
    }

    @Override
    public @NotNull ChatModule getModule() {
        return chatModule;
    }

    @Override
    public @NotNull String getName() {
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

    @Override
    public @Nullable String broadcast(@Nullable Entity sender, @NotNull String message, @Nullable Consumer<String> sendMessageConsumer) {
        ILoggedChat.super.broadcast(sender, message, sendMessageConsumer);
        return null;
    }
}
