package ru.levap.cms.controller.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.levap.cms.exceptions.NotFoundException;
import ru.levap.cms.persistance.model.Folder;
import ru.levap.cms.persistance.repository.FolderRepository;
import ru.levap.cms.service.storage.StorageFileNotFoundException;
import ru.levap.cms.service.storage.StorageService;

@Controller
public class FileUploadController {

	@Autowired
	private StorageService storageService;
	
	@Autowired
	private FolderRepository folderRepository;


	@GetMapping("/filesa/{id}/{size}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable Long id, @PathVariable String size) {
		Optional<Folder> folder = folderRepository.findById(id);
		if(folder.isPresent()) {
			Resource file = storageService.loadAsResource(folder.get(), size);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
		}
        throw new NotFoundException();
	}
	
	@GetMapping("/files/{id}/{size}")
	@ResponseBody
	public ResponseEntity<Resource> serveImage(@PathVariable Long id, @PathVariable String size) {
		Optional<Folder> folder = folderRepository.findById(id);
		if(folder.isPresent()) {
			Resource file = storageService.loadAsResource(folder.get(), size);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
		}
        throw new NotFoundException();
	}

	@Secured("ROLE_SUPERADMIN")
	@PostMapping("/files/upload/{id}")
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes, @PathVariable("id") Long id) {
		Optional<Folder> folder = folderRepository.findById(id);
		if(folder.isPresent()) {
			storageService.store(file, folder.get());
			return ResponseEntity.ok().build();
		}
        throw new NotFoundException();
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
}
