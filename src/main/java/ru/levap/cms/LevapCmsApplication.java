package ru.levap.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ru.levap.cms.service.folder.FolderProperties;
import ru.levap.cms.service.picture.PictureProperties;
import ru.levap.cms.service.storage.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class, PictureProperties.class, FolderProperties.class})
public class LevapCmsApplication {
	public static void main(String[] args) {
		SpringApplication.run(LevapCmsApplication.class, args);
	}
}
