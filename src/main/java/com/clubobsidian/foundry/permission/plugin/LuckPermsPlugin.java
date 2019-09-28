package com.clubobsidian.foundry.permission.plugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.clubobsidian.foundry.Foundry;
import com.clubobsidian.foundry.permission.PermissionPlugin;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.event.EventHandler;
import me.lucko.luckperms.api.event.user.UserDataRecalculateEvent;

public class LuckPermsPlugin extends PermissionPlugin {

	private EventHandler<UserDataRecalculateEvent> handler;
	public LuckPermsPlugin() 
	{
		super("LuckPerms");
	}

	@Override
	public PermissionPlugin register() 
	{
		EventBus eventBus = LuckPerms.getApi().getEventBus();
		this.handler = eventBus.subscribe(UserDataRecalculateEvent.class, event -> 
		{
			User user = event.getUser();
			UUID uuid = user.getUuid();
			Bukkit.getScheduler().runTask(Foundry.get(), () ->
			{
				Player player = Bukkit.getServer().getPlayer(uuid);
				if(player != null)
				{
					this.updatePermissions(player);
				}
			});
		});
		return this;
	}

	@Override
	public boolean unregister() 
	{
		return this.handler.unregister();
	}
}