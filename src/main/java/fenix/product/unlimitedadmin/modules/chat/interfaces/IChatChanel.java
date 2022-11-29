package fenix.product.unlimitedadmin.modules.chat.interfaces;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.ChatModuleConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
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
    default String broadcast(@Nullable Entity sender, @NotNull String message, @Nullable Consumer<String> sendMessageConsumer) {
        final List<Player> targetPlayers = getTargetPlayers(sender, null);
        final String formattedMessage = formatMessage(sender, message);
        if (sendMessageConsumer != null) {
            sendMessageConsumer.accept(formattedMessage);
        }
        if (targetPlayers.isEmpty() || (targetPlayers.size() == 1 && targetPlayers.get(0) == sender)) {
            if (sender instanceof Player) {
                sender.sendMessage(formattedMessage);
            }
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
    default List<Player> getTargetPlayers(@Nullable Entity sender, @Nullable List<String> filteredNicknames) {
        final List<Player> onlinePlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers());
        if (filteredNicknames == null || filteredNicknames.isEmpty()) {
            return onlinePlayers;
        }
        return onlinePlayers.stream().filter(player -> filteredNicknames.contains(player.getName())).collect(Collectors.toList());
    }


    default String formatMessage(@Nullable Entity sender, @NotNull String message) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        String format = PlaceHolderUtils.replacePlayerPlaceholders(getFormat(), player);
        return format.replace("%message", message);
    }
}
