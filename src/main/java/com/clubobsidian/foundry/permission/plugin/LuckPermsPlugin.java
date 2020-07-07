package com.clubobsidian.foundry.permission.plugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.clubobsidian.foundry.FoundryPlugin;
import com.clubobsidian.foundry.permission.PermissionPlugin;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.PermissionData;
import me.lucko.luckperms.api.context.ContextManager;
import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.event.EventHandler;
import me.lucko.luckperms.api.event.user.UserDataRecalculateEvent;

public class LuckPermsPlugin extends PermissionPlugin {

	private EventHandler<UserDataRecalculateEvent> handler;
	
	public LuckPermsPlugin() {
		super("LuckPerms");
	}

	@Override
	public PermissionPlugin register() {
		LuckPermsApi api = LuckPerms.getApi();
		EventBus eventBus = api.getEventBus();
		this.handler = eventBus.subscribe(UserDataRecalculateEvent.class, event -> {
			User user = event.getUser();
			UUID uuid = user.getUuid();
			Bukkit.getScheduler().runTask(FoundryPlugin.get(), () -> {
				Player player = Bukkit.getServer().getPlayer(uuid);
				if(player != null) {
					this.updatePermissions(player);
				}
			});
		});
		return this;
	}

	@Override
	public boolean unregister() {
		return this.handler.unregister();
	}

	@Override
	public boolean hasPermission(Player player, String permission) {
		UUID uuid = player.getUniqueId();
		LuckPermsApi api = LuckPerms.getApi();
		User user = api.getUser(uuid);
		ContextManager contextManager = api.getContextManager();
		Contexts contexts = contextManager.lookupApplicableContexts(user).orElseGet(contextManager::getStaticContexts);
		PermissionData permissionData = user.getCachedData().getPermissionData(contexts);
		return permissionData.getPermissionValue(permission).asBoolean();
	}
}