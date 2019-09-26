package com.clubobsidian.foundry.permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.clubobsidian.foundry.Foundry;
import com.clubobsidian.foundry.permission.event.PermissionUpdateEvent;
import com.clubobsidian.foundry.permission.plugin.LuckPermsUpdater;


public final class PermissionManager implements Listener {

	private Map<UUID, Map<String, PermissionNode>> userPermissionCache;
	private PermissionUpdater updater;
	public PermissionManager()
	{
		this.userPermissionCache = new HashMap<>();
		this.updater = this.findUpdater();
	}

	public boolean hasPermission(String permission, Player player)
	{
		UUID uuid = player.getUniqueId();
		Map<String, PermissionNode> nodes = this.userPermissionCache.get(uuid);
		if(nodes == null)
		{
			nodes = new HashMap<>();
			this.userPermissionCache.put(uuid, nodes);
		}

		PermissionNode node = nodes.get(permission);
		if(node != null)
		{
			return node.hasPermission();
		}

		boolean has = player.hasPermission(permission);
		nodes.put(permission, new PermissionNode(permission, has));
		return has;
	}

	public PermissionUpdater getUpdater()
	{
		return this.updater;
	}

	private PermissionUpdater findUpdater()
	{
		List<PermissionUpdater> updaters = new ArrayList<>();
		updaters.add(new LuckPermsUpdater());

		for(PermissionUpdater updater : updaters)
		{
			Plugin plugin = Bukkit.getServer()
					.getPluginManager()
					.getPlugin(updater.getPluginName());

			if(plugin != null)
			{
				return updater.register();
			}
		}

		Foundry.get().getLogger().info("No permission updater can be found, permissions will only update on relog!");
		return null;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPermissionUpdate(PermissionUpdateEvent event)
	{
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		Map<String, PermissionNode> nodes = this.userPermissionCache.get(uuid);
		if(nodes != null)
		{
			Iterator<Entry<String, PermissionNode>> it = nodes.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<String, PermissionNode> next = it.next();
				String permission = next.getKey();
				PermissionNode node = next.getValue();
				if(!player.hasPermission(permission))
				{
					node.setHasPermission(false);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void permissionCacheCleanup(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		this.userPermissionCache.remove(uuid);
	}
}