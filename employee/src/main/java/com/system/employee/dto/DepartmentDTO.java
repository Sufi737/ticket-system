package com.system.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO extends RepresentationModel<DepartmentDTO> {
    private Integer id;
    private String departmentName;
}
