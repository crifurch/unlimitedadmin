package fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels;

import fenix.product.unlimitedadmin.modules.chat.interfaces.IChatChanel;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class FirewallChatChannel implements IChatChanel {
    private final IChatChanel child;

    public FirewallChatChannel(IChatChanel child) {
        this.child = child;
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
    public @NotNull List<Player> getTargetPlayers(@Nullable Entity sender, @Nullable List<String> filteredNicknames) {
        return child.getTargetPlayers(sender, filteredNicknames);
    }

    @Override
    public String formatMessage(@Nullable Entity sender, @NotNull String message) {
        return child.formatMessage(sender, message);
    }

    @Override
    public @Nullable String broadcast(@Nullable Entity sender, @NotNull String message, @Nullable Consumer<String> sendMessageConsumer) {
        if (isBlocked(sender, message)) {
            if (sendMessageConsumer != null) {
                sendMessageConsumer.accept(child.formatMessage(sender, message));
            }
            return getBlockedMessage(sender, message);
        }

        return child.broadcast(sender, message, sendMessageConsumer);
    }

    public boolean isBlocked(Entity sender, String message) {
        return false;
    }

    public String getBlockedMessage(Entity sender, String message) {
        return null;
    }
}
