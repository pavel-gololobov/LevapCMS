package ru.levap.cms.service.folder;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("folder")
public class FolderProperties {
	private Integer pageSize = 16;
}
