package com.system.employee.dto;

import com.system.employee.entities.Role;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class RoleDTO extends RepresentationModel<Role> {
    private Integer id;
    private String roleName;
}
