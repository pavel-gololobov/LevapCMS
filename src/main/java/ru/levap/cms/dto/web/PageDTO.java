package ru.levap.cms.dto.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
	private Integer pageNumber;
	private Integer pagesCount;
}
