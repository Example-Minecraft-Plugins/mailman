package me.davipccunha.tests.mailman.command.subcommand;

import org.bukkit.entity.Player;

public interface CorreioSubCommand {
    boolean execute(Player player, String[] args);

    String getUsage();
}
