package com.example.employeeapi;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import com.example.employeeapi.repository.EmployeeSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryIntegrationTest {

    @Autowired
    private EmployeeRepository repo;

    @Test
    void hql_native_and_spec_searches_work() {
        Employee e1 = new Employee("Sannee", "Challa", "sannee@gmail.com", "9998887776", "Tirupati");
        Employee e2 = new Employee("Bharath", "Kumar", "bharath@gmail.com", "9876543210", "Hyderabad");
        repo.save(e1);
        repo.save(e2);

        Optional<Employee> byEmailHql = repo.findByEmailHql("sannee@gmail.com");
        assertThat(byEmailHql).isPresent();

        Optional<Employee> byEmailNative = repo.findByEmailNative("sannee@gmail.com");
        assertThat(byEmailNative).isPresent();

        List<Employee> byNameHql = repo.findByNameHql("Sannee");
        assertThat(byNameHql).isNotEmpty();

        List<Employee> byNameNative = repo.findByNameNative("Sannee");
        assertThat(byNameNative).isNotEmpty();

        Specification<Employee> spec = EmployeeSpecifications.nameContains("Sannee");
        List<Employee> specList = repo.findAll(spec);
        assertThat(specList).isNotEmpty();
    }
}

