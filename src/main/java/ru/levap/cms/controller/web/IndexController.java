package ru.levap.cms.controller.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ru.levap.cms.dto.mapper.FolderMapper;
import ru.levap.cms.exceptions.NotFoundException;
import ru.levap.cms.persistance.model.Folder;
import ru.levap.cms.persistance.repository.FolderRepository;

@Controller
public class IndexController {
	@Autowired
	private FolderRepository folderRepository;
	
    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("folderId", null);
        return "stream";
    }
    
    @GetMapping("/root")
    public String rootFolder(Model model) {
        model.addAttribute("folderId", null);
        return "folder";
    }

    @GetMapping(value = "/folder/{id}")
    public String folder(Model model, @PathVariable("id") Long id) {
    	Optional<Folder> folder = folderRepository.findById(id);
		if(folder.isPresent()) {
			model.addAttribute("folder", FolderMapper.toDTO(folder.get()));
			model.addAttribute("folderId", id);
	        return "folder";
		}
        throw new NotFoundException();
    }
}
