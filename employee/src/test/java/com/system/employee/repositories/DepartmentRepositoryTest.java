package com.system.employee.repositories;

import com.system.employee.entities.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @MockBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void Find__By_Department_Name_Returns_Department() {
        Department department = departmentRepository.findByDepartmentName("CMS");
        assertThat(department).isNotNull();
    }
}
