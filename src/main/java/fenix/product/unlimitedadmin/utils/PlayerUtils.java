package fenix.product.unlimitedadmin.utils;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import net.querz.nbt.io.NBTInputStream;
import net.querz.nbt.io.NBTOutputStream;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.DoubleTag;
import net.querz.nbt.tag.FloatTag;
import net.querz.nbt.tag.ListTag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.*;
import java.util.UUID;

public class PlayerUtils {

    public static void setLocation(UUID uuid, Location location) {
        Player player = null;
        for (Player i : Bukkit.getOnlinePlayers()) {
            if (i.getUniqueId().equals(uuid)) {
                player = i;
                break;
            }
        }
        if (player != null) {
            player.teleport(location);
            return;
        }
        File file = new File("world/playerdata/" + uuid.toString() + ".dat");
        NamedTag tag = null;
        try {
            tag = NBTUtil.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final ListTag<DoubleTag> pos = (ListTag<DoubleTag>) ((CompoundTag) tag.getTag()).getListTag("Pos");
        pos.clear();
        pos.add(new DoubleTag(location.getX()));
        pos.add(new DoubleTag(location.getY()));
        pos.add(new DoubleTag(location.getZ()));

        final ListTag<FloatTag> rotation = (ListTag<FloatTag>) ((CompoundTag) tag.getTag()).getListTag("Rotation");
        rotation.clear();
        rotation.add(new FloatTag(location.getYaw()));
        rotation.add(new FloatTag(location.getPitch()));
        ((CompoundTag) tag.getTag()).putString("Dimension", location.getWorld().getEnvironment().name());

        try {
            NBTUtil.write(tag, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
