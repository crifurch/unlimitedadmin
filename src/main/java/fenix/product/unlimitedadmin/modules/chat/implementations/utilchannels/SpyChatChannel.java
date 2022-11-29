package fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels;

import fenix.product.unlimitedadmin.api.UnlimitedAdminPermissionsList;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.ChatModuleConfig;
import fenix.product.unlimitedadmin.modules.chat.data.Mute;
import fenix.product.unlimitedadmin.modules.chat.interfaces.IChatChanel;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ISpiedChat;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ISubhandlerChannel;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class SpyChatChannel implements ISubhandlerChannel {


    final ChatModule chatModule;

    public SpyChatChannel(ChatModule chatModule) {
        this.chatModule = chatModule;
    }

    @Override
    public @NotNull ChatModule getModule() {
        return chatModule;
    }

    @Override
    public @Nullable String getChannelPrefix() {
        return null;
    }

    @Override
    public @NotNull String getName() {
        return "spy";
    }

    @Override
    public String onSubhandlerMessage(@Nullable Entity sender, IChatChanel parent, String message) {
        final IChatChanel iChatChanel = ChatModule.unwrapChannel(parent);
        if (!(iChatChanel instanceof ISpiedChat)) {
            return null;
        }
        final List<Player> parentTargetPlayers = parent.getTargetPlayers(sender, null);
        String parentFormattedMessage = iChatChanel.formatMessage(sender, message);
        if (ChatModuleConfig.SPY_CHAT_SHOW_CHANNEL.getBoolean()) {
            parentFormattedMessage = ((ISpiedChat) iChatChanel).getSpyPrefix() + " " + parentFormattedMessage;
        }
        final String formattedMessage = formatMessage(sender, parentFormattedMessage);
        getTargetPlayers(sender, null).forEach(targetPlayer -> {
            boolean muted = false;
            if (sender instanceof Player) {
                if (PermissionsProvider.getInstance().havePermission((Player) sender,
                        UnlimitedAdminPermissionsList.CHAT_MUTE_BYPASS) != PermissionStatus.PERMISSION_TRUE) {
                    final Mute mute = chatModule.getMute((sender).getUniqueId());
                    if (mute != null) {
                        muted = mute.isMuted(getName());
                    }
                }
            }
            if (parentTargetPlayers.contains(targetPlayer) && !muted) {
                return;
            }
            targetPlayer.sendMessage(formattedMessage);
        });
        return null;
    }


    @Override
    public @NotNull List<Player> getTargetPlayers(@Nullable Entity sender, @Nullable List<String> filteredNicknames) {
        final List<Player> targetPlayers = ISubhandlerChannel.super.getTargetPlayers(sender, filteredNicknames);
        return targetPlayers.stream().filter(player -> player != sender && PermissionsProvider.getInstance().
                havePermission(player, UnlimitedAdminPermissionsList.CHAT_SPY) == PermissionStatus.PERMISSION_TRUE
        ).collect(Collectors.toList());
    }

    @Override
    public @NotNull String getFormat() {
        return ChatModuleConfig.SPY_CHAT_FORMAT.getString();
    }

}
