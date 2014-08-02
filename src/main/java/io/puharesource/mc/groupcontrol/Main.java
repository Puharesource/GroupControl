package io.puharesource.mc.groupcontrol;

import io.puharesource.mc.groupcontrol.listeners.ListenerChat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Permission permission = null;

    public static Map<String, List<String>> rankTypes = new HashMap<>();
    public static Map<String, Rank> ranks = new HashMap<>();
    public static Map<String, String> rankLayouts = new HashMap<>();

    public static Rank getRank(String rank) {
        return ranks.get(rank);
    }

    public void onEnable() {
        if (!setupPermissions()) {
            getLogger().log(Level.SEVERE, "Couldn't setup permissions! Disabling plugin.");
            getPluginLoader().disablePlugin(this);
        }
        loadConfig();
        loadSettings();

        getServer().getPluginManager().registerEvents(new ListenerChat(), this);
    }

    public void loadConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        File config = new File(getDataFolder(), "config.yml");

        try {
            if (!config.exists()) Files.copy(getResource("config.yml"), config.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        getConfig();
        saveConfig();
    }

    public void loadSettings() {
        ConfigurationSection layoutSection = getConfig().getConfigurationSection("layouts");
        ConfigurationSection rankTypesSection = getConfig().getConfigurationSection("ranks");

        for (String layout : layoutSection.getKeys(false))
            rankLayouts.put(layout.toUpperCase(), layoutSection.getString(layout));

        for (String rankType : rankTypesSection.getKeys(false)) {
            rankType = rankType.toUpperCase();
            ConfigurationSection typeSection = rankTypesSection.getConfigurationSection(rankType);
            List<String> list = new ArrayList<>();
            list.addAll(typeSection.getKeys(false));
            rankTypes.put(rankType, list);

            for (String rank : typeSection.getKeys(false)) {
                ConfigurationSection rankSection = typeSection.getConfigurationSection(rank);
                ranks.put(rank, new Rank(rank, rankSection.getString("chatcolor"), rankSection.getString("displayNameColor"), rankSection.getString("tag"), rankType, rankSection.getInt("priority")));
            }
        }
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}
