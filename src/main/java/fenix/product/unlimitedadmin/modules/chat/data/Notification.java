package fenix.product.unlimitedadmin.modules.chat.data;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.implementations.channels.NotificationsChatChannel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class Notification implements Runnable {
    private final String message;
    private final int interval;
    private final ChatModule module;

    private final Consumer<String> onSendMessagesConsumer;

    private BukkitTask task;

    public Notification(ChatModule chatModule, String message, int interval, Consumer<String> onSendMessagesConsumer) {
        this.message = NotificationsChatChannel.CHANNEL_PREFIX + message;
        this.interval = 20 * interval;
        this.module = chatModule;
        this.onSendMessagesConsumer = onSendMessagesConsumer;
    }

    public Notification(ChatModule chatModule, String message, int interval) {
        this.message = NotificationsChatChannel.CHANNEL_PREFIX + message;
        this.interval = 20 * interval;
        this.module = chatModule;
        this.onSendMessagesConsumer = null;
    }

    public void stop() {
        if (task != null) task.cancel();
    }

    public void schedule() {
        task = Bukkit.getScheduler().runTaskLater(UnlimitedAdmin.getInstance(), this, interval);
    }

    public void start() {
        if (task != null) return;
        run();
    }

    public String getMessage() {
        return message;
    }

    public int getInterval() {
        return interval;
    }

    @Override
    public void run() {
        module.broadcastMessage(null, message);
        if (interval > 0) {
            task = Bukkit.getScheduler().runTaskLater(UnlimitedAdmin.getInstance(), this, interval);
        } else {
            stop();
        }
        if (onSendMessagesConsumer != null) onSendMessagesConsumer.accept(message);
    }

    public ConfigurationSection toConfig(ConfigurationSection section) {
        section.set("message", message);
        section.set("interval", interval / 20);
        return section;
    }
}
