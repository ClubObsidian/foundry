package com.clubobsidian.foundry;

import org.bukkit.plugin.java.JavaPlugin;

import com.clubobsidian.foundry.permission.PermissionManager;

public class Foundry extends JavaPlugin {

	private static Foundry instance;
	
	private PermissionManager permissionManager;
	
	@Override
	public void onEnable()
	{
		instance = this;
		this.permissionManager = new PermissionManager();
		if(this.permissionManager.getPlugin() == null)
		{
			this.getPluginLoader().disablePlugin(this);
		}
		else
		{
			this.getLogger().info("Foundry is now enabled!");
		}
	}
	
	public static Foundry get()
	{
		return instance;
	}
	
	public PermissionManager getPermissionManager()
	{
		return this.permissionManager;
	}
}