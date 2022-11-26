package fenix.product.unlimitedadmin.modules.chat.data;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.implementations.channels.NotificationsChatChannel;
import org.bukkit.Bukkit;

public class AdsNotification implements Runnable {
    private final String message;
    private final int interval;
    private final ChatModule module;

    private boolean running = true;

    public AdsNotification(ChatModule chatModule, String message, int interval) {
        assert interval > 0;
        this.message = NotificationsChatChannel.CHANNEL_PREFIX + message;
        this.interval = 20 * interval;
        this.module = chatModule;
        run();
    }

    public void cancel() {
        running = false;
    }

    public String getMessage() {
        return message;
    }

    public int getInterval() {
        return interval;
    }

    @Override
    public void run() {
        if (!running) return;
        module.broadcastMessage(null, message);
        Bukkit.getServer().getScheduler().runTaskLater(UnlimitedAdmin.getInstance(), this, interval);
    }
}
