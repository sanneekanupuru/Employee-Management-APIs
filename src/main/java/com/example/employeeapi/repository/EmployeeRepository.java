package com.example.employeeapi.repository;

import com.example.employeeapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmail(String email);
    List<Employee> findByFirstNameIgnoreCase(String firstName);
    List<Employee> findByLastNameIgnoreCase(String lastName);
    boolean existsByEmail(String email);

    // HQL / JPQL
    @Query("SELECT e FROM Employee e WHERE e.email = :email")
    Optional<Employee> findByEmailHql(@Param("email") String email);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) = LOWER(:name) OR LOWER(e.lastName) = LOWER(:name)")
    List<Employee> findByNameHql(@Param("name") String name);

    // Native SQL
    @Query(value = "SELECT * FROM employees WHERE email = :email", nativeQuery = true)
    Optional<Employee> findByEmailNative(@Param("email") String email);

    @Query(value = "SELECT * FROM employees WHERE LOWER(first_name) = LOWER(:name) OR LOWER(last_name) = LOWER(:name)", nativeQuery = true)
    List<Employee> findByNameNative(@Param("name") String name);
}
