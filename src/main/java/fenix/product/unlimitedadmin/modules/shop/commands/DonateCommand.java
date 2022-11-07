package fenix.product.unlimitedadmin.modules.shop.commands;

import fenix.product.unlimitedadmin.LangConfig;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.shop.ShopModule;
import fenix.product.unlimitedadmin.modules.shop.ShopModuleConfig;
import fenix.product.unlimitedadmin.modules.shop.data.PlayerDonationCache;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class DonateCommand implements ICommand {
    private final ShopModule module;

    public DonateCommand(ShopModule shopModule) {
        this.module = shopModule;
    }

    @Override
    public String getUsageText() {
        return ICommand.super.getUsageText() + " <amount>";
    }

    @Override
    public byte getMaxArgsSize() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "donate";
    }


    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        return Arrays.asList("10RUB", "20RUB", "30RUB", "40RUB", "50RUB", "60RUB", "70RUB",
                "80RUB", "90RUB", "100RUB", "200RUB",
                "300RUB", "400RUB", "500RUB", "600RUB",
                "700RUB", "800RUB", "900RUB", "1000RUB");
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LangConfig.ONLY_FOR_PLAYER_COMMAND.getText());
            return true;
        }
        if (argsString.size() == 0) {
            final PlayerDonationCache donationCache = module.getDonationCache((Player) sender);
            sender.sendMessage(LangConfig.DONATION_AMOUNT.getText(donationCache.getAmount()));
            return true;
        }
        final String amount = argsString.get(0);
        sender.sendMessage(ShopModuleConfig.SHOP_DONATE_PAGE_URL.getText(amount));
        return true;
    }

}
