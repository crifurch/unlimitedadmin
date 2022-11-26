package fenix.product.unlimitedadmin.modules.chat.implementations.channels;

import fenix.product.unlimitedadmin.modules.chat.ChatModuleConfig;
import fenix.product.unlimitedadmin.modules.chat.interfaces.IChatChanel;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ILoggedChat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class GlobalChatChannel implements ILoggedChat, IChatChanel {

    @Override
    public String getName() {
        return "global";
    }

    @Override
    public String getChannelPrefix() {
        return ChatModuleConfig.GLOBAL_CHAT_PREFIX.getString();
    }


    @Override
    public @NotNull List<Player> getTargetPlayers(@Nullable Entity sender, @Nullable List<String> filteredNicknames) {
        List<Player> targetPlayers = ILoggedChat.super.getTargetPlayers(sender, filteredNicknames);
        if (sender instanceof Player && !ChatModuleConfig.IS_GLOBAL_CHAT_WORLD_WIDE.getBoolean()) {
            final Player player = (Player) sender;
            targetPlayers = targetPlayers.stream().filter(targetPlayer -> targetPlayer.getWorld() != player.getWorld()).collect(Collectors.toList());
        }
        return targetPlayers;
    }

    @Override
    public @NotNull String getFormat() {
        return ChatModuleConfig.GLOBAL_CHAT_FORMAT.getString();
    }
}

