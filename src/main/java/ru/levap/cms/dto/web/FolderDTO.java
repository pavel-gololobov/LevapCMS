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
public class FolderDTO {
	private Long id;
    private Boolean active;
    private String name;
    private String description;
    private String picture;
    private Long createDate;
    private Integer position;
    private Long updateDate;
    private Long parentId;
    private Long frontImageId;
    private Long previousId;
    private Long nextId;
    private Boolean stream;
    private List<FolderParameterDTO> parameters;
}
