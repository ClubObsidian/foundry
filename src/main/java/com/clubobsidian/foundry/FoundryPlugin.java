package com.clubobsidian.foundry;

import com.clubobsidian.foundry.permission.PermissionManagerImpl;
import com.clubobsidian.foundry.permission.PermissionPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.clubobsidian.foundry.permission.PermissionManager;

import java.util.logging.Level;

public class FoundryPlugin extends JavaPlugin {

	private static FoundryPlugin instance;

	public static FoundryPlugin get() {
		return instance;
	}

	private PermissionManager permissionManager;

	@Override
	public void onEnable() {
		instance = this;
		this.permissionManager = new PermissionManagerImpl();
		if(this.permissionManager.getPlugin() == null) {
			this.getLogger().log(Level.SEVERE, "No permission updater found, disabling...");
			this.getPluginLoader().disablePlugin(this);
		} else {
			this.getServer().getPluginManager().registerEvents(this.permissionManager, this);
			this.getLogger().log(Level.INFO, "Foundry is now enabled!");
		}
	}

	@Override
	public void onDisable() {
		PermissionPlugin plugin = this.permissionManager.getPlugin();
		if (plugin != null) {
			plugin.unregister();
		}
	}

	public PermissionManager getPermissionManager() {
		return this.permissionManager;
	}
}