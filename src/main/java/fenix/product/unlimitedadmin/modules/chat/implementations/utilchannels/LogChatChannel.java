package fenix.product.unlimitedadmin.modules.chat.implementations.utilchannels;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.interfaces.IChatChanel;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ILoggedChat;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ISubhandlerChannel;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class LogChatChannel implements ISubhandlerChannel {
    private static Pattern pattern = Pattern.compile("§.");
    final ChatModule chatModule;
    private File logFile;

    public LogChatChannel(ChatModule chatModule) {
        this.chatModule = chatModule;
    }


    @Override
    public String getName() {
        return "log";
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public String onSubhandlerMessage(@Nullable Entity sender, IChatChanel parent, String message) {
        final IChatChanel iChatChanel = ChatModule.unwrapChannel(parent);
        if (!(iChatChanel instanceof ILoggedChat)) {
            return null;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[dd/MM/yyyy HH:mm:ss]");
        DateTimeFormatter dtFile = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        LocalDateTime now = LocalDateTime.now();
        String logPrefix = ((ILoggedChat) iChatChanel).getLogPrefix();
        String logFileName = dtFile.format(now) + ".log";
        if (logFile == null || !logFile.getName().equals(logFileName) || !logFile.exists()) {
            logFile = new File(UnlimitedAdmin.getInstance().getModuleFolder(chatModule), "logs");
            if (!logFile.exists()) {
                logFile.mkdirs();
            }
            logFile = new File(logFile, logFileName);
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        logFile.setWritable(true);

        String formatMessage = pattern.matcher(iChatChanel.formatMessage(sender, message)).replaceAll("");
        String logMessage = dtf.format(now) + " " + logPrefix + " " + formatMessage;
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(logFile, true));
            output.append(logMessage).append(System.lineSeparator());
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
