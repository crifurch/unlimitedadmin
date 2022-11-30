package fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels;

import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.data.sender.ChatMessageSender;
import fenix.product.unlimitedadmin.modules.chat.interfaces.IChatChanel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class FirewallChatChannel implements IChatChanel {

    final ChatModule chatModule;

    private final IChatChanel child;

    public FirewallChatChannel(ChatModule chatModule, IChatChanel child) {
        this.chatModule = chatModule;
        this.child = child;
    }

    @Override
    public @NotNull ChatModule getModule() {
        return chatModule;
    }

    public IChatChanel getChild() {
        return child;
    }

    @Override
    public @NotNull String getName() {
        return child.getName();
    }

    @Override
    public String getChannelPrefix() {
        return child.getChannelPrefix();
    }

    @Override
    public @NotNull String getFormat() {
        return child.getFormat();
    }

    @Override
    public @NotNull List<Player> getTargetPlayers(@NotNull ChatMessageSender sender, @Nullable List<String> filteredNicknames) {
        return child.getTargetPlayers(sender, filteredNicknames);
    }

    @Override
    public String formatMessage(@NotNull ChatMessageSender sender, @NotNull String message) {
        return child.formatMessage(sender, message);
    }

    @Override
    public @Nullable String broadcast(@NotNull ChatMessageSender sender, @NotNull String message, @Nullable Consumer<String> sendMessageConsumer) {
        if (sendMessageConsumer != null) {
            sendMessageConsumer.accept(child.formatMessage(sender, message));
        }
        if (isBlocked(sender, message)) {
            return getBlockedMessage(sender, message);
        }

        return child.broadcast(sender, message, sendMessageConsumer);
    }

    public boolean isBlocked(ChatMessageSender sender, String message) {
        return false;
    }

    public String getBlockedMessage(ChatMessageSender sender, String message) {
        return null;
    }
}
