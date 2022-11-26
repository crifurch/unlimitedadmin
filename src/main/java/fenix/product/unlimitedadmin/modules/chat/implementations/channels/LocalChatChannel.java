package fenix.product.unlimitedadmin.modules.chat.implementations.channels;

import fenix.product.unlimitedadmin.modules.chat.ChatModuleConfig;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ILoggedChat;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ISpiedChat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class LocalChatChannel implements ILoggedChat, ISpiedChat {
    @Override
    public String getName() {
        return "local";
    }

    @Override
    public @Nullable String getChannelPrefix() {
        return ChatModuleConfig.LOCAL_CHAT_PREFIX.getString();
    }

    @Override
    public @NotNull List<Player> getTargetPlayers(@Nullable Entity sender, @Nullable List<String> filteredNicknames) {
        List<Player> targetPlayers = ILoggedChat.super.getTargetPlayers(sender, filteredNicknames);
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final int squaredRadius = ChatModuleConfig.LOCAL_CHAT_RADIUS.getInt() * ChatModuleConfig.LOCAL_CHAT_RADIUS.getInt();
            targetPlayers = targetPlayers.stream().filter(targetPlayer -> {
                if (sender == targetPlayer) {
                    return true;
                }
                final boolean isInSameWorlds = targetPlayer.getWorld() == player.getWorld();
                final boolean isInRange = targetPlayer.getLocation().distanceSquared(player.getLocation()) <= squaredRadius;
                return isInSameWorlds && isInRange;
            }).collect(Collectors.toList());
        }
        return targetPlayers;
    }


    @Override
    public @NotNull String getFormat() {
        return ChatModuleConfig.LOCAL_CHAT_FORMAT.getString();
    }
}
