package me.ialistannen.consoleblock.commands;

import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.ialistannen.consoleblock.ConsoleBlock;
import me.ialistannen.consoleblock.filter.MessageFilter;

/**
 * The main command
 */
public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(getUsage());
            return true;
        }
        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "list": {
                if (!sender.hasPermission("consoleblock.list")) {
                    sender.sendMessage(ChatColor.RED + "No permission!");
                    return true;
                }
                return listFilters(sender);
            }
            case "reload": {
                if (!sender.hasPermission("consoleblock.reload")) {
                    sender.sendMessage(ChatColor.RED + "No permission!");
                    return true;
                }
                ConsoleBlock.getInstance().reloadFilters();
                sender.sendMessage(ChatColor.translateAlternateColorCodes(
                        '&',
                        String.format(
                                Locale.ENGLISH,
                                "&aLoaded &6%d &afilter(s).",
                                ConsoleBlock.getInstance().getFilterManager().getActiveFilters().size()
                        )
                ));
                break;
            }
            default: {
                sender.sendMessage(getUsage());
            }
        }
        return true;
    }

    private String getUsage() {
        return ChatColor.translateAlternateColorCodes(
                '&',
                "&cUsage: &6/consoleBlock &c<reload | list>"
        );
    }

    private boolean listFilters(CommandSender sender) {
        sender.sendMessage(" ");
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&',
                        "&8+&m------------------&a&l Filters &8&m------------------&8 +"
                )
        );
        sender.sendMessage(" ");

        List<MessageFilter> activeFilters = ConsoleBlock.getInstance().getFilterManager().getActiveFilters();

        for (int i = 0; i < activeFilters.size(); i++) {
            MessageFilter filter = activeFilters.get(i);

            sender.sendMessage(ChatColor.GRAY + "  > "
                    + ChatColor.GOLD + Integer.toString(i)
                    + ChatColor.DARK_GRAY + ": "
                    + ChatColor.GREEN + filter.getName()
            );
        }

        sender.sendMessage(" ");
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&',
                        "&8+&m--------------------------------------------&8 +"
                )
        );
        sender.sendMessage(" ");

        return true;
    }
}
