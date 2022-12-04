package fenix.product.unlimitedadmin.modules.chat.commands;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.data.Mute;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MuteCommand implements ICommand {
    final ChatModule chatModule;

    public MuteCommand(ChatModule chatModule) {
        this.chatModule = chatModule;
    }

    @Override
    public @NotNull String getName() {
        return "mute";
    }

    @Override
    public String getUsageText() {
        return ICommand.super.getUsageText() + " <player> [channel]";
    }

    @Override
    public byte getMaxArgsSize() {
        return 2;
    }

    @Override
    public byte getMinArgsSize() {
        return 1;
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        if (i == 1) {
            final List<String> notificationNames = new ArrayList<>(chatModule.getChannelsList());
            notificationNames.add(0, "*");
            return notificationNames;
        }
        return ICommand.super.getTabCompletion(sender, args, i);
    }

    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws NotifibleException {
        String playerName = args.get(0);
        final UUID playerUUID = UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerUUID(playerName);
        if (playerUUID == null) {
            throw new NotifibleException(LangConfig.NO_SUCH_PLAYER.getText(playerName));
        }
        String channel = "*";
        if (args.count() == 2) {
            channel = args.get(1);
        }
        if (channel.equals("all")) {
            channel = "*";
        }
        if (!channel.equals("*") && !chatModule.getChannelsList().contains(channel)) {
            throw new NotifibleException(LangConfig.NO_SUCH_CHANNEL.getText(channel));
        }
        Mute mute = chatModule.getMute(playerUUID);
        if (mute == null) {
            mute = new Mute(playerUUID, new ArrayList<>());
        }
        mute.addMutedChannel(channel);
        chatModule.setMute(mute);
    }
}
