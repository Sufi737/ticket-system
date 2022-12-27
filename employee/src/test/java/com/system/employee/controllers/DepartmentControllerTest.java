package com.system.employee.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.employee.dto.DepartmentDTO;
import com.system.employee.entities.Department;
import com.system.employee.rest.DepartmentController;
import com.system.employee.services.DepartmentService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void Map_Department_To_DepartmentDTO() {
        this.modelMapper = new ModelMapper();
        Department department = new Department(1, "Tech");
        DepartmentDTO deptDTO = new DepartmentDTO(1, "Tech");
        assertThat(this.modelMapper.map(department, DepartmentDTO.class)).isEqualTo(deptDTO);
    }

    @Test
    void Get_Department_Returns_200_Ok_When_Department_Present() throws Exception {
        Optional<Department> deptOptional = Optional.of(new Department(1, "Tech"));
        when(departmentService.getDepartment(1)).thenReturn(deptOptional);
        assertThat(departmentService.getDepartment(1)).isEqualTo(deptOptional);
        mockMvc.perform(get("/department/{id}", 1)
            .contentType("application/json")
        ).andExpect(status().isOk());
    }

    @Test
    void Get_Department_Returns_Department() throws Exception{
        Department department = new Department(1, "Tech");
        Optional<Department> deptOptional = Optional.of(department);
        when(departmentService.getDepartment(1)).thenReturn(deptOptional);
        assertThat(departmentService.getDepartment(1)).isEqualTo(deptOptional);
        DepartmentDTO deptDTO = new DepartmentDTO(1, "Tech");

        ResultActions resultActions = mockMvc.perform(get("/department/{id}", 1)
            .contentType("application/json")
        );
        resultActions.andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.departmentName").value("Tech"));
    }

    @Test
    void Get_Department_Throws_Exception_When_Department_Not_Present() throws Exception {
        Optional<Department> deptOptional = Optional.ofNullable(null);
        when(departmentService.getDepartment(1)).thenReturn(deptOptional);
        mockMvc.perform(get("/department/{id}", 1)
            .contentType("application/json")
        ).andExpect(status().isNotFound());
    }

    @Test
    void Create_Department_Creates_New_Department() throws Exception {
        Department department = new Department(1, "Tech");
        when(departmentService.createDepartment(department)).thenReturn(department);
        DepartmentDTO deptDTO = new DepartmentDTO(1, "Tech");

        ResultActions resultActions = mockMvc.perform(post("/department")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(department))
        );
        resultActions.andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.departmentName").value("Tech"));
    }

    @Test
    void Create_Department_Throws_Exception_When_Department_Exists_With_Same_Name() throws Exception {
        Department department = new Department(1, "Tech");
        when(departmentService.createDepartment(department)).thenReturn(null);
        mockMvc.perform(post("/department")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(department))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void Update_Department_Updates_Existing_Department() throws Exception {
        Department department = new Department(1, "Tech");
        Department updatedDepartment = new Department(1, "CMS");
        when(departmentService.updateDepartment(department)).thenReturn(updatedDepartment);
        ResultActions resultActions = mockMvc.perform(put("/department")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(department))
        );
        resultActions.andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.id").value(updatedDepartment.getId()))
            .andExpect(jsonPath("$.departmentName").value(updatedDepartment.getDepartmentName()));
    }

    @Test
    void Update_Department_Throws_Exception_When_Id_Not_Provided() throws Exception {
        Department department = new Department(null, "CMS");
        mockMvc.perform(put("/department")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(department))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void Update_Department_Throws_Exception_When_Department_Not_Present() throws Exception {
        Department department = new Department(1, "Tech");
        when(departmentService.updateDepartment(department)).thenReturn(null);
        mockMvc.perform(put("/department")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(department))
        ).andExpect(status().isNotFound());
    }

    @Test
    void Delete_Department_When_Department_Present() throws Exception {
        Department department = new Department(1, "Tech");
        doNothing().when(departmentService).deleteDepartment(1);
        mockMvc.perform(delete("/department/{id}", 1)
            .contentType("application/json")
        ).andExpect(status().isOk());
    }
}
