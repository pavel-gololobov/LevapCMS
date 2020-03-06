package ru.levap.cms.dto.common;

public enum UserRoleEnum {
	
	USER,
	DISPATCHER,
	ADMIN,
	SUPERADMIN;
	
	public String toRoleName() {
		return "ROLE_" + this.name();
	}

	public static UserRoleEnum fromRoleName(String role) {
		return UserRoleEnum.valueOf(role.replace("ROLE_", ""));
	}

}
