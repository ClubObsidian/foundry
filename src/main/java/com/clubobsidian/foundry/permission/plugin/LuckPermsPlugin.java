package com.clubobsidian.foundry.permission.plugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.clubobsidian.foundry.FoundryPlugin;
import com.clubobsidian.foundry.permission.PermissionPlugin;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.EventSubscription;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

public class LuckPermsPlugin extends PermissionPlugin {

	private EventSubscription<UserDataRecalculateEvent> handler;
	
	public LuckPermsPlugin() {
		super("LuckPerms");
	}

	@Override
	public PermissionPlugin register() {
		LuckPerms api = this.getLuckPerms();
		EventBus eventBus = api.getEventBus();
		this.handler = eventBus.subscribe(UserDataRecalculateEvent.class, event -> {
			User user = event.getUser();
			UUID uuid = user.getUniqueId();
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
		this.handler.close();
		return true;
	}

	@Override
	public boolean hasPermission(Player player, String permission) {
		UUID uuid = player.getUniqueId();
		LuckPerms api = this.getLuckPerms();
		User user = api.getUserManager().getUser(uuid);
		ContextManager contextManager = api.getContextManager();
		ImmutableContextSet contexts = contextManager.getContext(user).orElseGet(contextManager::getStaticContext);
		CachedPermissionData permissionData = user.getCachedData().getPermissionData(QueryOptions.contextual(contexts));
		return permissionData.checkPermission(permission).asBoolean();
	}

	private LuckPerms getLuckPerms() {
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		return provider.getProvider();
	}
}