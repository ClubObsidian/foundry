package com.clubobsidian.foundry.permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.clubobsidian.foundry.permission.event.PermissionUpdateEvent;


public abstract class PermissionPlugin {

	private String pluginName;
	
	public PermissionPlugin(String pluginName) {
		this.pluginName = pluginName;
	}
	
	public String getPluginName() {
		return this.pluginName;
	}
	
	public void updatePermissions(Player player) {
		Bukkit.getServer()
		.getPluginManager()
		.callEvent(new PermissionUpdateEvent(player));
	}
	
	public abstract PermissionPlugin register();
	public abstract boolean unregister();
	public abstract boolean hasPermission(Player player, String permission);
}