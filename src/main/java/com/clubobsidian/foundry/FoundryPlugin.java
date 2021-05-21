package com.clubobsidian.foundry;

import org.bukkit.plugin.java.JavaPlugin;

import com.clubobsidian.foundry.permission.PermissionManager;

public class FoundryPlugin extends JavaPlugin {

	private static FoundryPlugin instance;
	
	public static FoundryPlugin get() {
		return instance;
	}
	
	private PermissionManager permissionManager;
	
	@Override
	public void onEnable() {
		instance = this;
		this.permissionManager = new PermissionManager();
		if(this.permissionManager.getPlugin() == null) {
			this.getPluginLoader().disablePlugin(this);
		} else {
			this.getServer().getPluginManager().registerEvents(this.permissionManager, this);
			this.getLogger().info("Foundry is now enabled!");
		}
	}
	
	public PermissionManager getPermissionManager() {
		return this.permissionManager;
	}
}