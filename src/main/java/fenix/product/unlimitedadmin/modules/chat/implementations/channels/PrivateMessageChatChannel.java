package fenix.product.unlimitedadmin.modules.chat.implementations.channels;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.ChatModuleConfig;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ILoggedChat;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ISpiedChat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PrivateMessageChatChannel implements ILoggedChat, ISpiedChat {
    public static final String CHANNEL_PREFIX = "!notification!";
    private final ChatModule chatModule;

    public PrivateMessageChatChannel(ChatModule chatModule) {
        this.chatModule = chatModule;
    }

    @NotNull
    public static String prepareMessage(@NotNull String message, Entity... receivers) {
        final String receiversNames = String.join(",", Arrays.stream(receivers).map(Entity::getName).toArray(String[]::new));
        return CHANNEL_PREFIX + "%" + receiversNames + "%" + message;
    }

    @NotNull
    public static String prepareMessage(@NotNull String message, String... receivers) {
        final String receiversNames = String.join(",", receivers);
        return CHANNEL_PREFIX + "%" + receiversNames + "%" + message;
    }

    @Override
    public String getName() {
        return "private";
    }

    @Override
    public @Nullable String getChannelPrefix() {
        return CHANNEL_PREFIX;
    }

    @Override
    public @Nullable String broadcast(@Nullable Entity sender, @NotNull String message) {
        if (message.startsWith("%")) {
            return LangConfig.NO_SUCH_PLAYER.getText();
        }
        int index = message.indexOf("%", 1);
        final String targetName = message.substring(1, index);
        List<String> filteredNicknames = new ArrayList<>();
        if (targetName.contains(",")) {
            String[] split = targetName.split(",");
            Collections.addAll(filteredNicknames, split);
        } else {
            filteredNicknames.add(targetName);
        }
        final List<Player> targetPlayers = getTargetPlayers(sender, filteredNicknames);
        final String formattedMessage = formatMessage(sender, message.substring(index + 1));
        if (targetPlayers.isEmpty()) {
            if (!ChatModuleConfig.SHOW_NOBODY_HEAR_YOU_MESSAGE.getBoolean()) {
                return null;
            }
            return LangConfig.CHAT_NOBODY_HEAR.getText();
        }
        targetPlayers.forEach(player -> {
            chatModule.addForAnswer(sender, player);
            final String message1 = formatForRecipient(player, formattedMessage);
            player.sendMessage(message1);
            if (sender != null) {
                sender.sendMessage(message1);
            }
        });
        return null;
    }

    @Override
    public @NotNull String getFormat() {
        return ChatModuleConfig.PRIVATE_CHAT_FORMAT.getString();
    }

    private @NotNull String formatForRecipient(@NotNull Player recipient, @NotNull String message) {
        return PlaceHolderUtils.replacePlayerPlaceholders(message, null, recipient);
    }
}
