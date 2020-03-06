package ru.levap.cms.controller.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ru.levap.cms.dto.web.FolderDTO;
import ru.levap.cms.exceptions.NotFoundException;
import ru.levap.cms.persistance.model.Folder;
import ru.levap.cms.persistance.repository.FolderRepository;
import ru.levap.cms.service.folder.FolderService;

@Controller
public class PictureController {

	@Autowired
	private FolderRepository folderRepository;
	
	@Autowired
	private FolderService folderService;
	
    @GetMapping(value = "/picture/{id}")
    public String picture(Model model, @PathVariable("id") Long id) {
    	Optional<Folder> folder = folderRepository.findById(id);
		if(folder.isPresent()) {
			FolderDTO folderDTO = folderService.getOneFolderWithExif(folder.get(), false);
			model.addAttribute("folder", folderDTO);
			model.addAttribute("folderId", id);
	        return "picture";
		}
        throw new NotFoundException();
    }
    
    @GetMapping(value = "/stream/{id}")
    public String streamPicture(Model model, @PathVariable("id") Long id) {
    	Optional<Folder> folder = folderRepository.findById(id);
		if(folder.isPresent()) {
			FolderDTO folderDTO = folderService.getOneFolderWithExif(folder.get(), true);
			model.addAttribute("folder", folderDTO);
			model.addAttribute("folderId", id);
	        return "picture";
		}
        throw new NotFoundException();
    }
}
