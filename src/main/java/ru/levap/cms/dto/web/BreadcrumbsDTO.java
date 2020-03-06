package ru.levap.cms.dto.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.levap.cms.dto.common.BreadcrumbsNodeType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreadcrumbsDTO {
	private Long id;
	private BreadcrumbsNodeType type;
	private String name;
}
