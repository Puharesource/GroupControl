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
    public void onChat(AsyncPlayerChatEvent event) {
        Rank rank = getMostDominantRank(event.getPlayer());

        if (rank != null)
            event.setMessage(rank.getChatColor() + event.getMessage());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChatHP(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        getMostDominantRank(player);

        event.setFormat(formatChat(player, event.getFormat()));
    }

    private String formatChat(Player player, String format) {
        Rank dominantRank = getMostDominantRank(player);
        if(dominantRank != null)
            format = format.replaceAll("(?i)\\{" + "GCDISPLAYNAME" + "\\}", dominantRank.getDisplayNameColor() + player.getDisplayName());
        else format = format.replaceAll("(?i)\\{" + "GCDISPLAYNAME" + "\\}", player.getDisplayName());

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
        Integer priority = null;
        for (String type : Main.rankTypes.keySet()) {
            Rank rank1 = getRankByType(player, type);
            if(rank == null)
                rank = rank1;
            else if(priority == null)
                priority = Main.rankTypePriorities.get(type);
            else{
                int priority1 = Main.rankTypePriorities.get(type);
                if(priority < priority1) {
                    priority = priority1;
                    rank = rank1;
                }
            }
        }
        return rank;
    }

    private Rank getRankByType(Player player, String type) {
        Rank highestPriorityRank = null;
        Integer priority = null;
        String[] groups = Main.permission.getPlayerGroups(player);
        if (groups == null) return null;
        if (groups.length <= 0) return null;

        for (String group : groups) {
            Rank rank = Main.getRank(group);

            if (rank == null) continue;
            if (!rank.getType().equalsIgnoreCase(type)) continue;

            if (highestPriorityRank == null) {
                highestPriorityRank = rank;
                priority = rank.getPriority();
            } else {
                if(priority > rank.getPriority()) continue;
                highestPriorityRank = rank;
                priority = rank.getPriority();
            }
        }
        return highestPriorityRank;
    }
}
