package me.davipccunha.tests.mailman.command;

import me.davipccunha.tests.mailman.MailmanPlugin;
import me.davipccunha.tests.mailman.command.subcommand.CorreioSubCommand;
import me.davipccunha.tests.mailman.command.subcommand.EnviarSubCommand;
import me.davipccunha.tests.mailman.command.subcommand.VerSubCommand;
import me.davipccunha.tests.mailman.factory.view.MailboxGUI;
import me.davipccunha.tests.mailman.model.Mailbox;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CorreioCommand implements CommandExecutor {
    private static String COMMAND_USAGE;
    private final MailmanPlugin plugin;
    private final Map<String, CorreioSubCommand> subCommands = new HashMap<>();

    public CorreioCommand(MailmanPlugin plugin) {
        this.plugin = plugin;

        this.subCommands.put("enviar", new EnviarSubCommand(plugin));
        this.subCommands.put("ver", new VerSubCommand(plugin));

        this.updateUsage();
    }

    private void updateUsage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("§e/correio [");
        for (String subCommand : this.subCommands.keySet()) {
            stringBuilder.append(subCommand).append(" | ");
        }
        stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length());
        stringBuilder.append("]");

        COMMAND_USAGE = stringBuilder.toString();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem executar este comando.");
            return false;
        }

        final Player player = (Player) sender;

        if (args.length == 0) {
            Bukkit.dispatchCommand(player, "correio ver " + player.getName());
            return true;
        }

        CorreioSubCommand subCommand = this.subCommands.get(args[0]);

        if (subCommand == null) {
            sender.sendMessage("§cSubcomando não encontrado.");
            sender.sendMessage("§cUso: " + COMMAND_USAGE);
            return false;
        }

        if (!subCommand.execute(player, args)) {
            sender.sendMessage("§cUso: " + subCommand.getUsage());
            return false;
        }

        return true;
    }
}
