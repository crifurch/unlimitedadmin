package fenix.product.unlimitedadmin.modules.world.commands;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandErrorException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import fenix.product.unlimitedadmin.modules.world.WorldsModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class DeleteCommand implements ICommand {
    private final WorldsModule manager;
    private boolean isBusy = false;

    public DeleteCommand(WorldsModule manager) {
        this.manager = manager;
    }

    @Override
    public byte getMinArgsSize() {
        return 1;
    }

    @Override
    public byte getMaxArgsSize() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "remove";
    }

    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws NotifibleException {
        if (isBusy) {
            throw new CommandErrorException(LangConfig.WORLD_DELETION_BUSY.getText());
        }

        isBusy = true;
        try {
            final String error = manager.deleteWorld(args.get(0));
            if (error != null) {
                throw new CommandErrorException(error);
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, e.toString());
            isBusy = false;
            throw new CommandErrorException(LangConfig.WORLD_DELETION_ERROR.getText(args.get(0)));
        }
        sender.sendMessage(LangConfig.WORLD_DELETED.getText(args.get(0)));
    }
}
