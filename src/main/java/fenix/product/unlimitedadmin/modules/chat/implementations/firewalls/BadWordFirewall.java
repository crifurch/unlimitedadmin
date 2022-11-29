package fenix.product.unlimitedadmin.modules.chat.implementations.firewalls;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.ChatModuleConfig;
import fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels.FirewallChatChannel;
import fenix.product.unlimitedadmin.modules.chat.interfaces.IChatChanel;
import org.bukkit.entity.Entity;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BadWordFirewall extends FirewallChatChannel {

    static File badWordsFile;
    static List<Pattern> badWords = new ArrayList<>();
    final ChatModule chatModule;


    public BadWordFirewall(IChatChanel child, ChatModule chatModule) {
        super(child);
        this.chatModule = chatModule;
        loadBadWords(chatModule);
    }

    static void loadBadWords(ChatModule chatModule) {
        if (badWordsFile == null) {
            badWordsFile = UnlimitedAdmin.getInstance().getModuleFile(chatModule, "badwords.txt");
        }
        badWords.clear();
        try {
            BufferedReader reader = new BufferedReader(new java.io.FileReader(badWordsFile));
            String line;
            while ((line = reader.readLine()) != null) {
                badWords.add(Pattern.compile(line, Pattern.CASE_INSENSITIVE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isBlocked(Entity sender, String message) {
        final List<String> stringList = ChatModuleConfig.BAD_WORDS_CHANNELS.getStringList();
        if (!(stringList.contains(getChild().getName()) || stringList.contains("*"))) {
            return false;
        }
        return badWords.stream().anyMatch(pattern -> pattern.matcher(message).find());
    }

    @Override
    public String getBlockedMessage(Entity sender, String message) {
        return LangConfig.CHAT_BAD_WORD_DETECTED.getText();
    }

}
