package fenix.product.unlimitedadmin.modules.shop;

import fenix.product.unlimitedadmin.ModulesManager;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.module.IModule;
import fenix.product.unlimitedadmin.modules.shop.commands.DonateCommand;
import fenix.product.unlimitedadmin.modules.shop.commands.ShopCommand;
import fenix.product.unlimitedadmin.modules.shop.data.DonationRepository;
import fenix.product.unlimitedadmin.modules.shop.data.PlayerDonationCache;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShopModule implements IModule {
    private final List<ICommand> commands = new ArrayList<>();
    private final List<PlayerDonationCache> donationCaches = new ArrayList<>();

    public ShopModule() {
        commands.add(new ShopCommand(this));
        commands.add(new DonateCommand(this));
        ShopModuleConfig.load();
        CommandsShopConfig.load();
    }

    @Override
    public @NotNull String getName() {
        return ModulesManager.SHOP.getName();
    }

    @Override
    public List<ICommand> getCommands() {
        return commands;
    }

    public PlayerDonationCache getDonationCache(Player player) {
        for (PlayerDonationCache cache : donationCaches) {
            if (cache.getPlayerName().equals(player.getName()) && !cache.isExpired()) {
                return cache;
            }
        }
        final PlayerDonationCache playerDonationAmount = DonationRepository.getPlayerDonationAmount(player.getName());
        donationCaches.add(playerDonationAmount);
        return playerDonationAmount;
    }

}
