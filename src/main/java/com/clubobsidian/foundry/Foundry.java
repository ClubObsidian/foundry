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
		this.getLogger().info("Foundry is now enabled and waiting for registration!");
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