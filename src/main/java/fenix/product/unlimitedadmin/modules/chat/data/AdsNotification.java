package fenix.product.unlimitedadmin.modules.chat.data;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.implementations.channels.NotificationsChatChannel;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class AdsNotification implements Runnable {
    private final String message;
    private final int interval;
    private final ChatModule module;

    private final Consumer<String> onSendMessagesConsumer;
    private boolean running = true;

    private BukkitTask task;

    public AdsNotification(ChatModule chatModule, String message, int interval, Consumer<String> onSendMessagesConsumer) {
        this.message = NotificationsChatChannel.CHANNEL_PREFIX + message;
        this.interval = 20 * interval;
        this.module = chatModule;
        this.onSendMessagesConsumer = onSendMessagesConsumer;
        task = Bukkit.getScheduler().runTaskLater(UnlimitedAdmin.getInstance(), this, this.interval);
    }

    public AdsNotification(ChatModule chatModule, String message, int interval) {
        this.message = NotificationsChatChannel.CHANNEL_PREFIX + message;
        this.interval = 20 * interval;
        this.module = chatModule;
        this.onSendMessagesConsumer = null;
        task = Bukkit.getScheduler().runTaskLater(UnlimitedAdmin.getInstance(), this, this.interval);
    }

    public void stop() {
        running = false;
        if (task != null) task.cancel();
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
        if (interval > 0) {
            task = Bukkit.getScheduler().runTaskLater(UnlimitedAdmin.getInstance(), this, interval);
        } else {
            running = false;
            task = null;
        }
        if (onSendMessagesConsumer != null) onSendMessagesConsumer.accept(message);
    }
}
