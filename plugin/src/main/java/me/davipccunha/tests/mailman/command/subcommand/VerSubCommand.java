package me.davipccunha.tests.mailman.command.subcommand;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.mailman.MailmanPlugin;
import me.davipccunha.tests.mailman.factory.view.MailboxGUI;
import me.davipccunha.tests.mailman.model.Mailbox;
import me.davipccunha.utils.cache.RedisCache;
import me.davipccunha.utils.messages.ErrorMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class VerSubCommand implements CorreioSubCommand {
    private final MailmanPlugin plugin;

    @Override
    public boolean execute(Player player, String[] args) {
        final RedisCache<Mailbox> cache = this.plugin.getMailboxCache();

        if (args.length < 2) {
            Bukkit.dispatchCommand(player, "correio ver " + player.getName());
            return true;
        }

        final String target = args[1].toLowerCase();

        if (target.equals(player.getName().toLowerCase())) {
            final Mailbox ownMailbox = cache.get(target.toLowerCase());
            if (ownMailbox == null) {
                player.sendMessage(ErrorMessages.INTERNAL_ERROR.getMessage());
                return true;
            }

            player.openInventory(MailboxGUI.createMailboxGUI(ownMailbox, false));
            return true;
        }

        if (!player.hasPermission("mailman.admin.see")) {
            player.sendMessage(ErrorMessages.NO_PERMISSION.getMessage());
            return true;
        }

        final Mailbox mailbox = cache.get(target);

        if (mailbox == null) {
            player.sendMessage(ErrorMessages.PLAYER_NOT_FOUND.getMessage());
            return true;
        }

        player.openInventory(MailboxGUI.createMailboxGUI(mailbox, true));

        return true;
    }

    @Override
    public String getUsage() {
        return "/correio ver <jogador>";
    }
}
