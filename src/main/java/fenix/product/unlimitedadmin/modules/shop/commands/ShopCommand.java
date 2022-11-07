package fenix.product.unlimitedadmin.modules.shop.commands;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.shop.CommandsShopConfig;
import fenix.product.unlimitedadmin.modules.shop.ShopModule;
import fenix.product.unlimitedadmin.modules.shop.data.CommandValueTemplate;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShopCommand implements ICommand {
    private final ShopModule module;

    public ShopCommand(ShopModule shopModule) {
        this.module = shopModule;
    }


    @Override
    public @NotNull String getName() {
        return "shop";
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        if (i == 0) {
            return Arrays.asList("command", "item");
        } else {
            if (args[0].equals("command")) {
                final List<String> prevArg = Arrays.asList(args).subList(1, Math.min(i, args.length));
                return CommandsShopConfig.getForTabCompletion(prevArg, i - 1);
            } else if (args[0].equals("item")) {
                //todo adds item when ready
                return null;
            }
        }
        return ICommand.super.getTabCompletion(sender, args, i);
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {

        return true;
    }
}
