package ru.levap.cms.service.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import ru.levap.cms.persistance.model.Folder;

public interface StorageService {
	void init();
	void store(MultipartFile file, Folder folder);
	Stream<Path> loadAll();
	Path load(String filename);
	Resource loadAsResource(Folder picture, String size);
}
