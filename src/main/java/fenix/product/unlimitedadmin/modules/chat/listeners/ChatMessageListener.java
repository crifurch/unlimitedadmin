package fenix.product.unlimitedadmin.modules.chat.listeners;

import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatMessageListener implements Listener {
    final ChatModule module;

    public ChatMessageListener(ChatModule module) {
        this.module = module;
    }

    @EventHandler(ignoreCancelled = true, priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onChatMessage(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        module.broadcastMessage(event.getPlayer(), event.getMessage());
    }
}
