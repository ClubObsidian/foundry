package com.clubobsidian.foundry.permission;

public class PermissionNode {

	private final String permission;
	private boolean hasPermission;
	
	public PermissionNode(String permission, boolean hasPermission) {
		this.permission = permission;
		this.hasPermission = hasPermission;
	}
	
	public String getPermission() {
		return this.permission;
	}
	
	public boolean hasPermission() {
		return this.hasPermission;
	}
	
	public void setHasPermission(boolean hasPermission) {
		this.hasPermission = hasPermission;
	}
}