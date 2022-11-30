package fenix.product.unlimitedadmin.modules.chat.implementations.firewalls;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.UnlimitedAdminPermissionsList;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.data.Mute;
import fenix.product.unlimitedadmin.modules.chat.data.sender.ChatMessageSender;
import fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels.FirewallChatChannel;
import fenix.product.unlimitedadmin.modules.chat.interfaces.IChatChanel;

public class MuteFirewall extends FirewallChatChannel {

    public MuteFirewall(IChatChanel child, ChatModule chatModule) {
        super(chatModule, child);
    }

    @Override
    public boolean isBlocked(ChatMessageSender sender, String message) {
        final Mute mute = getModule().getMute(sender.getUUID());
        if (mute == null) {
            return false;
        }
        return !(!mute.isMuted(getName()) ||
                sender.getPermissionStatus(UnlimitedAdminPermissionsList.CHAT_MUTE_BYPASS)
                        == PermissionStatus.PERMISSION_TRUE);
    }

    @Override
    public String getBlockedMessage(ChatMessageSender sender, String message) {
        return LangConfig.CHAT_MUTED_IN_CHANNEL.getText(getName());
    }
}
