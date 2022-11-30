package fenix.product.unlimitedadmin.modules.chat.data.sender;

import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityMessageSender extends ChatMessageSender {
    private final Entity entity;

    public EntityMessageSender(Entity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull String getName() {
        return entity.getName();
    }

    @Override
    public @NotNull UUID getUUID() {
        return entity.getUniqueId();
    }

    @Override
    public @NotNull PermissionStatus getPermissionStatus(String permission) {
        return PermissionStatus.PERMISSION_FALSE;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        entity.sendMessage(message);
    }

    @Override
    public boolean sameWorld(@NotNull World world) {
        return entity.getWorld() == world;
    }

    @Override
    public @NotNull String replacePlaceholders(@NotNull String message) {
        return PlaceHolderUtils.replacePlayerPlaceholders(message, entity);
    }

    @Override
    public boolean sameAs(Object obj) {
        if (obj instanceof EntityMessageSender) {
            return ((EntityMessageSender) obj).entity == entity;
        }
        if (obj instanceof Entity) {
            return obj == entity;
        }
        return false;
    }

    @Override
    public boolean inDistance(Location location, double squaredDistance) {
        return entity.getLocation().distanceSquared(location) <= squaredDistance;
    }
}
