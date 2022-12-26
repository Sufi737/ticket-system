package com.system.employee.dto;

import com.system.employee.entities.Department;
import com.system.employee.entities.Employee;
import com.system.employee.entities.Role;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class EmployeeDTO  extends RepresentationModel<EmployeeDTO> {
    private String firstname;
    private String lastname;
    private String email;
    private String code;
    private Employee manager;
    private Department department;
    private Role role;
}
