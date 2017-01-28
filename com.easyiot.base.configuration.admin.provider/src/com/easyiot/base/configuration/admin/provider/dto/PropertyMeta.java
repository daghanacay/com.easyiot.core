package com.easyiot.base.configuration.admin.provider.dto;

import org.osgi.dto.DTO;

public class PropertyMeta extends DTO {
	public String id;
	public String name;
	public boolean optional;
	public int type;
	public String value;
	public String description;
}
