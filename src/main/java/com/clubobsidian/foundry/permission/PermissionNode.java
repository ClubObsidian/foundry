package com.clubobsidian.foundry.permission;

import java.util.concurrent.atomic.AtomicBoolean;

public class PermissionNode {

	private final String permission;
	private final AtomicBoolean hasPermission;
	
	public PermissionNode(String permission, boolean hasPermission) {
		this.permission = permission;
		this.hasPermission = new AtomicBoolean(hasPermission);
	}
	
	public String getPermission() {
		return this.permission;
	}
	
	public boolean hasPermission() {
		return this.hasPermission.get();
	}
	
	public void setHasPermission(boolean hasPermission) {
		this.hasPermission.set(hasPermission);
	}
}