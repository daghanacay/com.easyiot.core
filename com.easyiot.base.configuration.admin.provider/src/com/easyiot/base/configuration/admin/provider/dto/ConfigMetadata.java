package com.easyiot.base.configuration.admin.provider.dto;

import java.util.List;

import org.osgi.dto.DTO;

public class ConfigMetadata extends DTO {
	public String pid;
	public String name;
	public String description;
	public List<PropertyMeta> properties;
}
