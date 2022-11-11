package fenix.product.unlimitedadmin.api.interfaces;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public interface ICommandDataProvider {

    String baseCommandPermission = "unlimitedadmin.commands.";

    @NotNull
    String getName();
    default byte getMaxArgsSize() {
        return Byte.MAX_VALUE;
    }

    default byte getMinArgsSize() {
        return 0;
    }

    default List<String> getPermissions() {
        return Collections.emptyList();
    }

    default boolean isNicknamesCompletionsAllowed() {
        return true;
    }

    default boolean isAutoSetPermission() {
        return true;
    }

    @Nullable
    default List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        return null;
    }

    default String getCommandPermission() {
        return baseCommandPermission + getName();
    }
}
