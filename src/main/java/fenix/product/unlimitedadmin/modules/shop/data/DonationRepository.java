package fenix.product.unlimitedadmin.modules.shop.data;

import fenix.product.unlimitedadmin.api.utils.HttpUtils;
import fenix.product.unlimitedadmin.modules.shop.ShopModuleConfig;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class DonationRepository {

    public static PlayerDonationCache getPlayerDonationAmount(String playerName) {
        PlayerDonationCache donationCache = new PlayerDonationCache(playerName, 0, LocalDateTime.now());
        try {
            final Map<String, Object> stringObjectMap = HttpUtils.readJsonFromUrl(ShopModuleConfig.SHOP_DONATE_URL.getText(playerName));
            if (stringObjectMap != null) {
                donationCache.fromJson(stringObjectMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return donationCache;
    }


}
