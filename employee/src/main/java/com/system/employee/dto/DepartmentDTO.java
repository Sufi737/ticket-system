package com.system.employee.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class DepartmentDTO extends RepresentationModel<DepartmentDTO> {
    private Integer id;
    private String departmentName;
}
