package ru.levap.cms.dto.mapper;

import ru.levap.cms.dto.web.FolderParameterDTO;
import ru.levap.cms.persistance.model.FolderParameter;

public class FolderParameterMapper {
	
	private FolderParameterMapper() {
	}
	
	public static FolderParameterDTO toDTO(FolderParameter param) {
		FolderParameterDTO paramDTO = FolderParameterDTO.builder() //
				.id(param.getId()) //
				.name(param.getName()) //
				.value(param.getValue()).build();
		return paramDTO;
	}
}
