package fenix.product.unlimitedadmin.modules.chat.implementations.firewalls;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.UnlimitedAdminPermissionsList;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.data.Mute;
import fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels.FirewallChatChannel;
import fenix.product.unlimitedadmin.modules.chat.interfaces.IChatChanel;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MuteFirewall extends FirewallChatChannel {
    final ChatModule chatModule;

    public MuteFirewall(IChatChanel child, ChatModule chatModule) {
        super(child);
        this.chatModule = chatModule;
    }

    @Override
    public boolean isBlocked(Entity sender, String message) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            final Mute mute = chatModule.getMute(player.getUniqueId());
            if (mute == null) {
                return false;
            }
            return !(!mute.isMuted(getName()) || PermissionsProvider.getInstance().havePermission(player,
                    UnlimitedAdminPermissionsList.CHAT_MUTE_BYPASS) == PermissionStatus.PERMISSION_TRUE);
        }
        return false;
    }

    @Override
    public String getBlockedMessage(Entity sender, String message) {
        return LangConfig.CHAT_MUTED_IN_CHANNEL.getText(getName());
    }
}
