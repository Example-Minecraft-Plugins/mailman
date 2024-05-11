package me.davipccunha.tests.mailman.command.subcommand;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.mailman.MailmanPlugin;
import me.davipccunha.tests.mailman.model.Mailbox;
import me.davipccunha.utils.cache.RedisCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class EnviarSubCommand implements CorreioSubCommand {
    private final MailmanPlugin plugin;

    @Override
    public boolean execute(Player player, String[] args) {
        final RedisCache<Mailbox> cache = this.plugin.getMailboxCache();

        if (!player.hasPermission("mailman.admin.send")) {
            player.sendMessage("§cVocê não tem permissão para executar este comando.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§cInforme o jogador que deseja enviar o item.");
            return false;
        }

        final String target = args[1];
        final Mailbox mailbox = cache.get(target);

        final ItemStack item = player.getItemInHand();

        if (item == null || item.getType().equals(Material.AIR)) {
            player.sendMessage("§cVocê precisa ter o item que deseja enviar em mãos.");
            return true;
        }

        if (target.equals("@a")) {
            this.sendToEveryone(item);
            player.sendMessage("§aItem enviado para todos os jogadores.");
            return true;
        }

        if (mailbox == null) {
            player.sendMessage("§cJogador não encontrado.");
            return true;
        }

        mailbox.addItem(item);
        cache.add(target, mailbox);

        player.sendMessage(String.format("§aItem enviado para o correio de §f%s §acom sucesso.", target));

        return true;
    }

    private void sendToEveryone(ItemStack item) {
        final RedisCache<Mailbox> cache = this.plugin.getMailboxCache();
        for (Mailbox mailbox : cache.getValues()) {
            mailbox.addItem(item);
            cache.add(mailbox.getOwner(), mailbox);
        }
    }

    @Override
    public String getUsage() {
        return "§e/correio enviar <jogador>";
    }
}
