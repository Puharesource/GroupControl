package io.puharesource.mc.groupcontrol.listeners;

import io.puharesource.mc.groupcontrol.Main;
import io.puharesource.mc.groupcontrol.Rank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ListenerChat implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChatLP(AsyncPlayerChatEvent event) {
        Rank rank = getMostDominantRank(event.getPlayer());
        if (rank != null) {
            event.setFormat(rank.getDisplayNameColor() + event.getPlayer().getDisplayName());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Rank rank = getMostDominantRank(event.getPlayer());
        if (rank != null) {
            event.setMessage(rank.getChatColor() + event.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChatHP(AsyncPlayerChatEvent event) {
        event.setFormat(formatChat(event.getPlayer(), event.getFormat()));
    }

    private String formatChat(Player player, String format) {
        for (String type : Main.rankTypes.keySet()) {
            Rank rank = getRankByType(player, type);

            if (rank != null)
                format = format.replaceAll("(?i)\\{" + type.toUpperCase() + "\\}", ChatColor.translateAlternateColorCodes('&', Main.rankLayouts.get(type.toUpperCase()).replace("{TAG}", rank.getTag())));
            else format = format.replaceAll("(?i)\\{" + type.toUpperCase() + "\\}", "");
        }
        return format;
    }

    private Rank getMostDominantRank(Player player) {
        Rank rank = null;
        for (String type : Main.rankTypes.keySet()) {
            rank = getRankByType(player, type);
            if (rank != null)
                break;
        }
        return rank;
    }

    private Rank getRankByType(Player player, String type) {
        Rank highestPriorityRank = null;
        Integer priority = null;
        String[] groups = Main.permission.getPlayerGroups(player);
        if (groups != null)
            if (groups.length > 0)
                for (String group : Main.permission.getPlayerGroups(player)) {
                    Rank rank = Main.getRank(group);

                    if (rank != null) {
                        if (!rank.getType().equalsIgnoreCase(type)) continue;
                        if (highestPriorityRank == null) {
                            highestPriorityRank = rank;
                            priority = rank.getPriority();
                        } else if (priority < rank.getPriority()) {
                            highestPriorityRank = rank;
                            priority = rank.getPriority();
                        }
                    }
                }
        return highestPriorityRank;
    }
}
