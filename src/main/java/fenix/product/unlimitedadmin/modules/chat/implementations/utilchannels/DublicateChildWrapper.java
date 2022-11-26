package fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels;

import fenix.product.unlimitedadmin.modules.chat.interfaces.IChatChanel;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ISubhandlerChannel;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DublicateChildWrapper implements IChatChanel {
    private final IChatChanel child;
    private final ISubhandlerChannel dublicator;

    public DublicateChildWrapper(IChatChanel child, @Nullable ISubhandlerChannel dublicator) {
        this.child = child;
        this.dublicator = dublicator;
    }

    public IChatChanel getChild() {
        return child;
    }

    @Override
    public String getName() {
        return child.getName();
    }

    @Override
    public String getChannelPrefix() {
        return child.getChannelPrefix();
    }

    @Override
    public @Nullable String broadcast(@Nullable Entity sender, @NotNull String message) {
        final String broadcast = child.broadcast(sender, message);
        if (dublicator != null) {
            dublicator.onSubhandlerMessage(sender, child, message);
        }
        return broadcast;
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

}
