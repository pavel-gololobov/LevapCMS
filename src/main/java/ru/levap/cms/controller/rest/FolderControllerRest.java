package ru.levap.cms.controller.rest;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.levap.cms.dto.mapper.FolderMapper;
import ru.levap.cms.dto.web.BreadcrumbsDTO;
import ru.levap.cms.dto.web.ErrorDTO;
import ru.levap.cms.dto.web.FolderDTO;
import ru.levap.cms.dto.web.FolderListDTO;
import ru.levap.cms.exceptions.NotFoundException;
import ru.levap.cms.persistance.model.Folder;
import ru.levap.cms.persistance.repository.FolderRepository;
import ru.levap.cms.service.folder.FolderService;

@RestController
@RequestMapping("/api/folder")
public class FolderControllerRest {
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private FolderRepository folderRepository;
	
	@GetMapping(value ="/get/root")
    public FolderListDTO getRootFolders(Principal principal, @RequestParam(value = "page", required = true) Integer page) {
        return folderService.getFolders(null, principal, page, true);
    }
	
	@GetMapping(value ="/get/{id}/breadcrumbs")
    public List<BreadcrumbsDTO> breadcrumbs(@PathVariable("id") Long id) {
		Optional<Folder> folder = folderRepository.findById(id);
		if(folder.isPresent()) {
			return folderService.getBreadcrumbs(folder.get());
		}
        throw new NotFoundException();
    }
    
    @GetMapping(value ="/get/{id}")
    public FolderListDTO getFolders(Principal principal, @PathVariable("id") Long id, @RequestParam(value = "page", required = true) Integer page) {
    	Optional<Folder> folder = folderRepository.findById(id);
		if(folder.isPresent()) {
			return folderService.getFolders(folder.get(), principal, page, true);
		}
        throw new NotFoundException();
    }
    
    @GetMapping(value ="/getone/{id}")
    public FolderDTO getOneFolder(Principal principal, @PathVariable("id") Long id) {
    	Optional<Folder> folder = folderRepository.findById(id);
		if(folder.isPresent()) {
			return FolderMapper.toDTO(folder.get());
		}
        throw new NotFoundException();
    }
	
	@Secured("ROLE_SUPERADMIN")
	@PostMapping(value ="/create")
    public ErrorDTO create(Principal principal, @RequestBody FolderDTO folderDTO) {
		return folderService.createFolder(folderDTO, principal);
    }
	
	@Secured("ROLE_SUPERADMIN")
	@PostMapping(value ="/update")
    public ErrorDTO update(Principal principal, @RequestBody FolderDTO folderDTO) {
		Optional<Folder> folder = folderRepository.findById(folderDTO.getId());
		if(folder.isPresent()) {
			return folderService.updateFolder(folder.get(), folderDTO, principal);
		}
        throw new NotFoundException();
    }
	
	@Secured("ROLE_SUPERADMIN")
	@PostMapping(value ="/delete")
    public ErrorDTO delete(Principal principal, @RequestBody FolderDTO folderDTO) {
		Optional<Folder> folder = folderRepository.findById(folderDTO.getId());
		if(folder.isPresent()) {
			return folderService.deleteFolder(folder.get(), principal);
		}
        throw new NotFoundException();
    }
}
