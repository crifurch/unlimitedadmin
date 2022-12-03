package fenix.product.unlimitedadmin.modules.chat.implementations.firewalls;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.permissions.UnlimitedAdminPermissionsList;
import fenix.product.unlimitedadmin.api.providers.PluginFileProvider;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.ChatModuleConfig;
import fenix.product.unlimitedadmin.modules.chat.data.sender.ChatMessageSender;
import fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels.FirewallChatChannel;
import fenix.product.unlimitedadmin.modules.chat.interfaces.IChatChanel;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

//todo smart bad words such as i = 1 or a = а
public class BadWordFirewall extends FirewallChatChannel {

    static File badWordsFile;
    static List<Pattern> badWords = new ArrayList<>();


    public BadWordFirewall(IChatChanel child, ChatModule chatModule) {
        super(chatModule, child);
    }

    public static void loadBadWords(ChatModule chatModule) {
        if (badWordsFile == null) {
            badWordsFile = PluginFileProvider.UnlimitedAdmin.getModuleFile(chatModule.getDefinition(), "badwords.txt");
        }
        badWords.clear();
        try {
            //read UTF-8
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(Files.newInputStream(badWordsFile.toPath()), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("#")) {
                    continue;
                }
                badWords.add(Pattern.compile(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unloadBadWords() {
        badWords.clear();
    }

    @Override
    public boolean isBlocked(ChatMessageSender sender, String message) {
        if (sender.getPermissionStatus(UnlimitedAdminPermissionsList.CHAT_BADWORDS_BYPASS) == PermissionStatus.PERMISSION_TRUE) {
            return false;
        }

        final List<String> stringList = ChatModuleConfig.BAD_WORDS_CHANNELS.getStringList();
        if (!(stringList.contains(getChild().getName()) || stringList.contains("*"))) {
            return false;
        }
        final String toCheck = message.toLowerCase().replaceAll("\\b", "").replace("_", "");
        return badWords.stream().anyMatch(pattern -> pattern.matcher(toCheck).find());
    }

    @Override
    public String getBlockedMessage(ChatMessageSender sender, String message) {
        return LangConfig.CHAT_BAD_WORD_DETECTED.getText();
    }

}
