package com.clubobsidian.foundry.permission;

import com.clubobsidian.foundry.permission.event.PermissionRecalculateEvent;
import com.clubobsidian.foundry.permission.event.PermissionUpdateEvent;
import com.clubobsidian.foundry.permission.plugin.LuckPermsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PermissionManagerImpl implements PermissionManager {

    private final Map<UUID, Map<String, PermissionNode>> userPermissionCache = new ConcurrentHashMap<>();
    private final PermissionPlugin plugin;

    public PermissionManagerImpl() {
        this.plugin = this.findPlugin();
    }

    public boolean hasPermission(Player player, String permission) {
        UUID uuid = player.getUniqueId();
        Map<String, PermissionNode> nodes = this.userPermissionCache.get(uuid);
        if(nodes == null) {
            nodes = new ConcurrentHashMap<>();
            this.userPermissionCache.put(uuid, nodes);
        }
        PermissionNode node = nodes.get(permission);
        if(node != null) {
            return node.hasPermission();
        }
        boolean has = this.plugin.hasPermission(player, permission);
        nodes.put(permission, new PermissionNode(permission, has));
        return has;
    }

    public PermissionPlugin getPlugin() {
        return this.plugin;
    }

    private PermissionPlugin findPlugin() {
        Collection<PermissionPlugin> plugins = Arrays.asList(
                new LuckPermsPlugin()
        );
        for(PermissionPlugin updater : plugins) {
            Plugin plugin = Bukkit.getServer()
                    .getPluginManager()
                    .getPlugin(updater.getPluginName());
            if(plugin != null) {
                return updater.register();
            }
        }
        return null;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPermissionUpdate(PermissionRecalculateEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Map<String, PermissionNode> nodes = this.userPermissionCache.get(uuid);
        if(nodes != null) {
            Iterator<Map.Entry<String, PermissionNode>> it = nodes.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<String, PermissionNode> next = it.next();
                String permission = next.getKey();
                PermissionNode node = next.getValue();
                boolean hasPermission = this.plugin.hasPermission(player, permission);
                if(hasPermission != node.hasPermission()) {
                    node.setHasPermission(hasPermission);
                }
            }
        }
        PermissionUpdateEvent permissionEvent = new PermissionUpdateEvent(player);
        Bukkit.getServer().getPluginManager().callEvent(permissionEvent);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void permissionCacheCleanup(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        this.userPermissionCache.remove(uuid);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void permissionCacheCleanup(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        this.userPermissionCache.remove(uuid);
    }
}