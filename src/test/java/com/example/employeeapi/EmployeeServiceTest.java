package com.example.employeeapi;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import com.example.employeeapi.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repo;

    @InjectMocks
    private EmployeeService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchByEmailUsingHql_returnsEmployee() {
        String email = "test@example.com";
        Employee e = new Employee("Fn","Ln", email, "123", "addr");
        when(repo.findByEmailHql(email)).thenReturn(Optional.of(e));

        Optional<Employee> res = service.searchByEmailUsingHql(email);

        assertThat(res).isPresent();
        assertThat(res.get().getEmail()).isEqualTo(email);
        verify(repo, times(1)).findByEmailHql(email);
    }

    @Test
    void searchByNameUsingNative_returnsList() {
        String name = "sannee";
        Employee e1 = new Employee("Sannee", "Challa", "sannee@gmail.com", "999", "addr");
        when(repo.findByNameNative(name)).thenReturn(List.of(e1));

        List<Employee> res = service.searchByNameUsingNative(name);

        assertThat(res).hasSize(1);
        verify(repo, times(1)).findByNameNative(name);
    }
}

