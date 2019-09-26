package com.clubobsidian.foundry;

import org.bukkit.plugin.java.JavaPlugin;

public class Foundry extends JavaPlugin {

	private static Foundry instance;
	
	@Override
	public void onEnable()
	{
		instance = this;
		this.getLogger().info("Foundry is now enabled and waiting for registration!");
	}
	
	public static Foundry get()
	{
		return instance;
	}
}