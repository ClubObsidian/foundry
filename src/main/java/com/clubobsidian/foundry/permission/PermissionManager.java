package com.clubobsidian.foundry.permission;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public interface PermissionManager extends Listener {


	boolean hasPermission(Player player, String permission);

	PermissionPlugin getPlugin();

}