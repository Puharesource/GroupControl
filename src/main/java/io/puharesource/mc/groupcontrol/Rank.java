package io.puharesource.mc.groupcontrol;

import org.bukkit.ChatColor;

public class Rank {
    private String groupName;
    private String chatColor;
    private String displayNameColor;
    private String tag;
    private String type;
    private int priority;

    public Rank(String groupName, String chatColor, String displayNameColor, String tag, String type, int priority) {
        this.groupName = groupName;
        this.chatColor = ChatColor.translateAlternateColorCodes('&', chatColor);
        this.displayNameColor = ChatColor.translateAlternateColorCodes('&', displayNameColor);
        this.tag = tag;
        this.type = type.toUpperCase();
        this.priority = priority;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getChatColor() {
        return chatColor;
    }

    public String getDisplayNameColor() {
        return displayNameColor;
    }

    public String getTag() {
        return tag;
    }

    public String getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }
}
