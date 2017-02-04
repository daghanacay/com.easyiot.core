package com.easyiot.base.security.provider.dto;

import java.util.List;

import org.osgi.dto.DTO;

public class PermissionDto extends DTO {
	public String name;
	public List<String> group_names;
}
