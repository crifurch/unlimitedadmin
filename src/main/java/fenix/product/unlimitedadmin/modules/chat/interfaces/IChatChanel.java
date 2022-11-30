package fenix.product.unlimitedadmin.modules.chat.interfaces;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.ChatModuleConfig;
import fenix.product.unlimitedadmin.modules.chat.data.sender.ChatMessageSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface IChatChanel {
    @NotNull
    ChatModule getModule();

    @Nullable
    default String getChannelPrefix() {
        return null;
    }

    @NotNull
    String getName();

    @NotNull
    default String getFormat() {
        return "%message";
    }

    @Nullable
    default String broadcast(@NotNull ChatMessageSender sender, @NotNull String message, @Nullable Consumer<String> sendMessageConsumer) {
        final List<Player> targetPlayers = getTargetPlayers(sender, null);
        final String formattedMessage = formatMessage(sender, message);
        if (sendMessageConsumer != null) {
            sendMessageConsumer.accept(formattedMessage);
        }
        if (targetPlayers.isEmpty() || (targetPlayers.size() == 1 && sender.sameAs(targetPlayers.get(0)))) {
            sender.sendMessage(formattedMessage);
            if (!ChatModuleConfig.SHOW_NOBODY_HEAR_YOU_MESSAGE.getBoolean()) {
                return null;
            }
            return LangConfig.CHAT_NOBODY_HEAR.getText();
        }
        targetPlayers.forEach(player -> {
            if (!getModule().requestSendMessage(sender, player)) {
                return;
            }
            player.sendMessage(formattedMessage);
        });
        return null;
    }

    @NotNull
    default List<Player> getTargetPlayers(@NotNull ChatMessageSender sender, @Nullable List<String> filteredNicknames) {
        final List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (filteredNicknames == null || filteredNicknames.isEmpty()) {
            return onlinePlayers;
        }
        return onlinePlayers.stream().filter(player -> filteredNicknames.contains(player.getName())).collect(Collectors.toList());
    }


    default String formatMessage(@NotNull ChatMessageSender sender, @NotNull String message) {
        return sender.replacePlaceholders(getFormat().replace("%message", message));
    }
}
