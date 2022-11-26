package fenix.product.unlimitedadmin.modules.chat;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.IModule;
import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import fenix.product.unlimitedadmin.modules.chat.data.AdsNotification;
import fenix.product.unlimitedadmin.modules.chat.implementations.channels.GlobalChatChannel;
import fenix.product.unlimitedadmin.modules.chat.implementations.channels.LocalChatChannel;
import fenix.product.unlimitedadmin.modules.chat.implementations.channels.NotificationsChatChannel;
import fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels.DublicateChildWrapper;
import fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels.FirewallChatChannel;
import fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels.LogChatChannel;
import fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels.SpyChatChannel;
import fenix.product.unlimitedadmin.modules.chat.interfaces.IChatChanel;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ILoggedChat;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ISpiedChat;
import fenix.product.unlimitedadmin.modules.chat.listeners.ChatMessageListener;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChatModule implements IModule {


    final UnlimitedAdmin plugin;

    private final List<ICommand> commands = new ArrayList<>();
    private final List<IChatChanel> chatChannels = new ArrayList<>();
    private SpyChatChannel spyChatChannel;
    private LogChatChannel logChatChannel;
    private List<AdsNotification> adsNotifications = new ArrayList<>();

    public ChatModule(@NotNull UnlimitedAdmin plugin) {
        this.plugin = plugin;
        ChatModuleConfig.init(this);
        plugin.getServer().getPluginManager().registerEvents(new ChatMessageListener(this), plugin);
        if (ChatModuleConfig.ADS_ENABLED.getBoolean()) {
            adsNotifications.add(new AdsNotification(this, "&etest", 10));
        }

        if (ChatModuleConfig.IS_SPY_CHAT_ENABLED.getBoolean()) {
            spyChatChannel = new SpyChatChannel();
        }
        if (ChatModuleConfig.IS_LOG_CHAT_ENABLED.getBoolean()) {
            logChatChannel = new LogChatChannel(this);
        }
        addChatChannel(new NotificationsChatChannel());

        if (ChatModuleConfig.IS_GLOBAL_CHAT_ENABLED.getBoolean()) {
            addChatChannel(new GlobalChatChannel());
        }
        if (ChatModuleConfig.IS_LOCAL_CHAT_ENABLED.getBoolean()) {
            addChatChannel(new LocalChatChannel());
        }


    }

    public static IChatChanel unwrapChannel(IChatChanel chatChanel) {
        if (chatChanel instanceof DublicateChildWrapper) {
            return unwrapChannel(((DublicateChildWrapper) chatChanel).getChild());
        }
        if (chatChanel instanceof FirewallChatChannel) {
            return unwrapChannel(((FirewallChatChannel) chatChanel).getChild());
        }
        return chatChanel;
    }

    public void addChatChannel(IChatChanel chatChannel) {
        IChatChanel toAdd = chatChannel;
        if (unwrapChannel(toAdd) instanceof ISpiedChat && spyChatChannel != null) {
            toAdd = new DublicateChildWrapper(toAdd, spyChatChannel);
        }
        if (unwrapChannel(toAdd) instanceof ILoggedChat && logChatChannel != null) {
            toAdd = new DublicateChildWrapper(toAdd, logChatChannel);
        }
        chatChannels.add(toAdd);
        chatChannels.sort(Comparator.comparingInt(o -> {
            if (o.getChannelPrefix() == null) return 0;
            return -o.getChannelPrefix().length();
        }));
    }

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public List<ICommand> getCommands() {
        return commands;
    }

    public void broadcastMessage(@Nullable Entity sender, String message) {
        String messageToSend = message;
        for (IChatChanel chatChannel : chatChannels) {
            String channelPrefix = chatChannel.getChannelPrefix();
            IChatChanel toSend = null;
            if (channelPrefix != null && message.startsWith(channelPrefix)) {
                messageToSend = message.substring(channelPrefix.length());
                if (messageToSend.trim().isEmpty()) {
                    return;
                }
                toSend = chatChannel;
            }
            if (channelPrefix == null) {
                toSend = chatChannel;
            }
            if (toSend != null) {
                final String broadcast = toSend.broadcast(sender, messageToSend);
                if (broadcast != null && sender != null) {
                    sender.sendMessage(
                            PlaceHolderUtils.replacePlayerPlaceholders(null, broadcast)
                    );
                }
                return;
            }
        }
    }

}
