package com.system.employee.services;

import brave.ScopedSpan;
import brave.Tracer;
import com.system.employee.entities.Department;
import com.system.employee.repositories.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private Tracer tracer;

    @Mock
    Logger logger;

    @Mock
    private ScopedSpan scopedSpan;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void Get_Department_When_Department_Is_Present() {
        when(tracer.startScopedSpan("getDepartmentDatabaseCall")).thenReturn(scopedSpan);
        Optional<Department> department = Optional.of(new Department(1, "Tech"));
        when(departmentRepository.findById(1)).thenReturn(department);
        assertThat(departmentService.getDepartment(1)).isEqualTo(department);
        verify(scopedSpan, atLeastOnce()).tag("peer.service", "mysql");
        verify(scopedSpan, atLeastOnce()).annotate("Client received");
        verify(scopedSpan, atLeastOnce()).finish();
    }

    @Test
    void Return_Null_When_Department_Not_Present() {
        when(tracer.startScopedSpan("getDepartmentDatabaseCall")).thenReturn(scopedSpan);
        when(departmentRepository.findById(1)).thenReturn(null);
        assertThat(departmentService.getDepartment(1)).isEqualTo(null);
        verify(scopedSpan, atLeastOnce()).tag("peer.service", "mysql");
        verify(scopedSpan, atLeastOnce()).annotate("Client received");
        verify(scopedSpan, atLeastOnce()).finish();
    }

    @Test
    void Create_Department_When_No_Other_Department_Exists() {
        when(tracer.startScopedSpan("createDepartmentDatabaseCall")).thenReturn(scopedSpan);
        when(departmentRepository.findByDepartmentName("Tech")).thenReturn(null);
        Department department = new Department(1, "Tech");
        when(departmentRepository.save(department)).thenReturn(department);
        assertThat(departmentService.createDepartment(department)).isEqualTo(department);
        verify(scopedSpan, atLeastOnce()).tag("peer.service", "mysql");
        verify(scopedSpan, atLeastOnce()).annotate("Client received");
        verify(scopedSpan, atLeastOnce()).finish();
    }

    @Test
    void Return_Null_On_Create_Department_When_Department_Exists_With_Same_Name() {
        when(tracer.startScopedSpan("createDepartmentDatabaseCall")).thenReturn(scopedSpan);
        Department department = new Department(1, "Tech");
        when(departmentRepository.findByDepartmentName("Tech")).thenReturn(department);
        assertThat(departmentService.createDepartment(department)).isEqualTo(null);
    }

    @Test
    void Update_Department_When_Department_Present() {
        when(tracer.startScopedSpan("updateDepartmentDatabaseCall")).thenReturn(scopedSpan);
        Optional<Department> department = Optional.of(new Department(1, "Tech"));
        Department updatedDepartment = new Department(1, "Updated");
        when(departmentRepository.findById(1)).thenReturn(department);
        when(departmentRepository.save(updatedDepartment)).thenReturn(updatedDepartment);
        assertThat(departmentService.updateDepartment(updatedDepartment)).isEqualTo(updatedDepartment);
        verify(scopedSpan, atLeastOnce()).tag("peer.service", "mysql");
        verify(scopedSpan, atLeastOnce()).annotate("Client received");
        verify(scopedSpan, atLeastOnce()).finish();
    }

    @Test
    void Update_Department_Returns_Null_When_Department_Not_Present() {
        when(tracer.startScopedSpan("updateDepartmentDatabaseCall")).thenReturn(scopedSpan);
        when(departmentRepository.findById(1)).thenReturn(Optional.ofNullable(null));
        Department updatedDepartment = new Department(1, "Updated");
        assertThat(departmentService.updateDepartment(updatedDepartment)).isEqualTo(null);
    }

    @Test
    void Delete_Department() {
        when(tracer.startScopedSpan("deleteDepartmentDatabaseCall")).thenReturn(scopedSpan);
        doNothing().when(departmentRepository).deleteById(1);
        departmentService.deleteDepartment(1);
    }
}
