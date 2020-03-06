package ru.levap.cms.dto.mapper;

import java.util.ArrayList;

import ru.levap.cms.dto.web.FolderDTO;
import ru.levap.cms.persistance.model.Folder;

public class FolderMapper {
	
	private FolderMapper() {
	}
	
	public static FolderDTO toDTO(Folder folder) {
		FolderDTO folderDTO = FolderDTO.builder() //
				.id(folder.getId()) //
				.active(folder.getActive()) //
				.name(folder.getName()) //
				.picture(folder.getPicture()) //
				.description(folder.getDescription()) //
				.position(folder.getPosition()) //
				.createDate(folder.getCreateDate()) //
				.updateDate(folder.getUpdateDate()) //
				.parameters(new ArrayList<>()).build();
		if(folder.getFrontImage() != null) {
			folderDTO.setFrontImageId(folder.getFrontImage().getId());
		}
		return folderDTO;
	}
}
