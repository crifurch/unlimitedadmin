package fenix.product.unlimitedadmin.modules.antiop;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.IModule;
import fenix.product.unlimitedadmin.api.managers.ServerDataManager;
import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import fenix.product.unlimitedadmin.modules.antiop.commands.AntiOPGroupCommand;
import fenix.product.unlimitedadmin.modules.antiop.commands.AntiOpDeopCommand;
import fenix.product.unlimitedadmin.modules.antiop.commands.AntiOpOpCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class AntiOPModule implements IModule, Listener {
    final UnlimitedAdmin plugin;
    private File logFile;

    private final List<ICommand> commands;

    public AntiOPModule(UnlimitedAdmin plugin) {
        this.plugin = plugin;
        AntiOPConfig.load();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        commands = Arrays.asList(
                new AntiOPGroupCommand(this),
                new AntiOpDeopCommand(),
                new AntiOpOpCommand()
        );
    }

    @Override
    public @NotNull String getName() {
        return "antiop";
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        checkOps(event.getPlayer());
    }

    public void checkOps(Player... players) {
        for (Player player : players) {
            if (player.isOp()) {
                final List<String> stringList = AntiOPConfig.OP_LIST.getStringList();
                if (!(stringList.contains(player.getName())
                        || stringList.contains(player.getUniqueId().toString()))) {
                    deOP(player);
                }
            }
        }
    }

    private void deOP(Player player) {
        if (player.isOp()) {
            ServerDataManager.setOP(player.getName(), false);
        }
        if (AntiOPConfig.LOG.getBoolean()) {
            logOp(player);
        }
        final int delay = AntiOPConfig.DELAY_BEFORE_COMMANDS.getInt();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (String command : AntiOPConfig.ADDITIONAL_RUN_COMMANDS.getStringList()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceHolderUtils.replacePlayerPlaceholders(command, player));
            }
        }, delay * 20L);

    }

    private void logOp(Player player) {
        if (logFile == null) {
            logFile = UnlimitedAdmin.getInstance().getModuleFile(this, "antiop.log");
        }
        try {
            final OutputStreamWriter out = new OutputStreamWriter(Files.newOutputStream(logFile.toPath(), StandardOpenOption.APPEND), StandardCharsets.UTF_8);
            BufferedWriter output = new BufferedWriter(out);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[dd/MM/yyyy HH:mm:ss]");
            LocalDateTime now = LocalDateTime.now();
            output.append(dtf.format(now))
                    .append(" ")
                    .append(player.getName())
                    .append(" tried to be op")
                    .append(System.lineSeparator());
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<ICommand> getCommands() {
        return commands;
    }
}