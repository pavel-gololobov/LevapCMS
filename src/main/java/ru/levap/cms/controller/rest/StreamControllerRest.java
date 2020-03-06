package ru.levap.cms.controller.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.levap.cms.dto.web.FolderListDTO;
import ru.levap.cms.service.folder.FolderService;

@RestController
@RequestMapping("/api/stream")
public class StreamControllerRest {
	@Autowired
	private FolderService folderService;
	
	@GetMapping(value ="/get")
    public FolderListDTO getStream(Principal principal, @RequestParam(value = "page", required = true) Integer page) {
        return folderService.getStream(principal, page);
    }
}
