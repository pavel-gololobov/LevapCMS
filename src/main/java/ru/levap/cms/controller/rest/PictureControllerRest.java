package ru.levap.cms.controller.rest;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.levap.cms.dto.web.ErrorDTO;
import ru.levap.cms.dto.web.FolderDTO;
import ru.levap.cms.exceptions.NotFoundException;
import ru.levap.cms.persistance.model.Folder;
import ru.levap.cms.persistance.repository.FolderRepository;
import ru.levap.cms.service.folder.FolderService;

@RestController
@RequestMapping("/api/picture")
public class PictureControllerRest {
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private FolderRepository folderRepository;
	
	@Secured("ROLE_SUPERADMIN")
	@PostMapping(value ="/setfront")
    public ErrorDTO create(Principal principal, @RequestBody FolderDTO folderDTO) {
		Optional<Folder> folder = folderRepository.findById(folderDTO.getId());
		if(folder.isPresent()) {
			return folderService.setAsFrontImage(folder.get(), principal);
		}
        throw new NotFoundException();
    }
	
	

}
