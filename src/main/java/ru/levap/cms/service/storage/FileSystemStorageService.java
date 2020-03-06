package ru.levap.cms.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ru.levap.cms.persistance.model.Folder;
import ru.levap.cms.persistance.model.FolderParameter;
import ru.levap.cms.service.folder.FolderService;
import ru.levap.cms.service.picture.PictureService;
import ru.levap.cms.service.picture.PictureSizeType;
import ru.levap.cms.utils.DateTimeUtils;

@Service
public class FileSystemStorageService implements StorageService {
	
	private final Path rootLocation;

	@Autowired
	private PictureService pictureService;
	
	@Autowired
	private FolderService folderService;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	@Transactional
	public void store(MultipartFile file, Folder folder) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException("Cannot store file with relative path outside current directory " + filename);
			}
			LocalDate date = LocalDate.now();
			Path targetDir = this.rootLocation.resolve(date.toString());
			Path targetPath = targetDir.resolve(filename);
			try (InputStream inputStream = file.getInputStream()) {
				Files.createDirectories(targetDir);
				Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
			}
			List<FolderParameter> params = pictureService.createImageThumbnail(targetPath, PictureSizeType.THUMBNAIL, true);
			pictureService.createImageThumbnail(targetPath, PictureSizeType.SMALL, false);
			pictureService.createImageThumbnail(targetPath, PictureSizeType.MEDIUM, false);
			pictureService.createImageThumbnail(targetPath, PictureSizeType.LARGE, false);
			
			Folder picture = folderService.createPicture(filename, folder);
			for(FolderParameter p : params) {
				p.setUpdateDate(DateTimeUtils.getLongTimeNow());
				p.setCreateDate(DateTimeUtils.getLongTimeNow());
				p.setFolder(picture);
			}
			folderService.saveFolderParameters(params);
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation)).map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(Folder picture, String size) {
		LocalDate uploadDate = LocalDateTime.ofEpochSecond(picture.getCreateDate(), 0, ZoneOffset.UTC).toLocalDate();
		try {
			PictureSizeType sizeType = PictureSizeType.valueOf(size.toUpperCase());
			String filename = FilenameUtils.getBaseName(picture.getPicture()) + "_" + sizeType.toString().toLowerCase() + ".jpg";
			Path file = rootLocation.resolve(uploadDate.toString()).resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file: " + file.toString());

			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file", e);
		}
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
