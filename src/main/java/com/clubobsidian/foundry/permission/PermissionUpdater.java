package com.clubobsidian.foundry.permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.clubobsidian.foundry.permission.event.PermissionUpdateEvent;


public abstract class PermissionUpdater {

	private String pluginName;
	public PermissionUpdater(String pluginName)
	{
		this.pluginName = pluginName;
	}
	
	public String getPluginName()
	{
		return this.pluginName;
	}
	
	public void updatePermissions(Player player)
	{
		Bukkit.getServer()
		.getPluginManager()
		.callEvent(new PermissionUpdateEvent(player));
	}
	
	public abstract PermissionUpdater register();
	public abstract boolean unregister();
}