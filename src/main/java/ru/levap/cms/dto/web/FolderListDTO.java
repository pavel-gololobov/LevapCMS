package ru.levap.cms.dto.web;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderListDTO {
	private List<FolderDTO> folders;
	private PageDTO page;
}
