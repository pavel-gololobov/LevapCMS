package ru.levap.cms.service.picture;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("picture")
public class PictureProperties {
	private Integer thumbSize = 300;
	private Integer smallSize = 600;
	private Integer mediumSize = 1200;
	private Integer largeSize = 2100;
}
