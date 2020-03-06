package ru.levap.cms.dto.web;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.levap.cms.dto.common.ErrorType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
	private ErrorType type;
	private String status;
    private String message;
    private List<String> errors;
}
