package com.clubobsidian.foundry.permission.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PermissionUpdateEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();
	
	public PermissionUpdateEvent(Player player) {
		super(player);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}