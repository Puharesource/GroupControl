package io.puharesource.mc.groupcontrol.commands;

import io.puharesource.mc.groupcontrol.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandGroupControl implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("chatcontrol")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("chatcontrol.reload")) {
                        Main.plugin.reloadConfig();
                        Main.plugin.loadSettings();
                        sender.sendMessage(ChatColor.GREEN + "You have reloaded the config!");
                    } else sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                } else syntaxError(sender);
            } else syntaxError(sender);
            return true;
        }
        return false;
    }

    private void syntaxError(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Wrong usage! Correct usage:");
        sender.sendMessage(ChatColor.GRAY + "    /chatcontrol reload" + ChatColor.RED + " - reloads the config!");
    }
}
