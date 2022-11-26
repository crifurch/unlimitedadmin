package fenix.product.unlimitedadmin.api.utils;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class RGBColors {

    private static final HashMap<String, String> placeHolderColorMap = new HashMap<>();

    private static Boolean supported = null;

    public static void load() {
        if (isNotSupported()) {
            return;
        }
//        ConfigurationSection configurationSection = Config.RGB_COLORS.getConfigurationSection();
//
//        if (configurationSection == null) {
//            ChatEx.getInstance().getLogger().info("No ColorCodes Specified!");
//            return;
//        }
//
//        for (Map.Entry<String, Object> stringObjectEntry : configurationSection.getValues(false).entrySet()) {
//            String key = stringObjectEntry.getKey();
//            String value = (String) stringObjectEntry.getValue();
//            LogHelper.debug(("Loading custom color code " + key + " with value " + value + " from config!"));
//            String clearedValue = value.replaceFirst("#", "");
//            char[] valueChars = clearedValue.toCharArray();
//            StringBuilder rgbColor = new StringBuilder();
//            rgbColor.append("§x");
//            for (int i = 0; i < clearedValue.length(); i++) {
//                rgbColor.append("§").append(valueChars[i]);
//            }
//            LogHelper.debug("Putting KEY: " + key + " value: " + rgbColor.toString());
//            placeHolderColorMap.put(key, rgbColor.toString());
//        }
    }

    public static String translateCustomColorCodes(String s) {
        if (isNotSupported()) {
            return s;
        }
        s = translateSingleMessageColorCodes(s);
        for (Map.Entry<String, String> stringColorEntry : placeHolderColorMap.entrySet()) {
            s = s.replace(stringColorEntry.getKey(), stringColorEntry.getValue());
        }
        return s;
    }

    public static String translateSingleMessageColorCodes(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.length() - i > 8) {
                String tempString = s.substring(i, i + 8);
                if (tempString.startsWith("&#")) {
                    char[] tempChars = tempString.replaceFirst("&#", "").toCharArray();
                    StringBuilder rgbColor = new StringBuilder();
                    rgbColor.append("§x");
                    for (char tempChar : tempChars) {
                        rgbColor.append("§").append(tempChar);
                    }
                    s = s.replaceAll(tempString, rgbColor.toString());
                }
            }
        }
        return s;
    }

    private static boolean isNotSupported() {
        if (supported == null) {
            try {
                final String version = Bukkit.getVersion();
                String ver = version.split("\\(MC: ")[1];
                String[] numbers = ver.replaceAll("\\)", "").split("\\.");
                ver = numbers[0] + numbers[1];
                int toCheck = Integer.valueOf(ver);
                supported = toCheck >= 116;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return !supported;
    }
}
