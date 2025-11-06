package com.example.employeeapi.service;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import com.example.employeeapi.repository.EmployeeSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    // Find by email
    public Optional<Employee> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    // Find by name (either first or last)
    public List<Employee> findByName(String name) {
        List<Employee> result = repo.findByFirstNameIgnoreCase(name);
        result.addAll(repo.findByLastNameIgnoreCase(name));
        return result;
    }

    // Create employee
    public Employee create(Employee e) {
        return repo.save(e);
    }

    // Update lastName, phone, address using email to find the employee
    public Employee updateDetailsByEmail(String email, String lastName, String phone, String address) {
        Optional<Employee> opt = repo.findByEmail(email);
        if (opt.isEmpty()) return null;
        Employee e = opt.get();
        if (lastName != null) e.setLastName(lastName);
        if (phone != null) e.setPhone(phone);
        if (address != null) e.setAddress(address);
        return repo.save(e);
    }

    // Update only phone by email
    public Employee updatePhoneByEmail(String email, String phone) {
        Optional<Employee> opt = repo.findByEmail(email);
        if (opt.isEmpty()) return null;
        Employee e = opt.get();
        e.setPhone(phone);
        return repo.save(e);
    }

    // Delete by email
    public boolean deleteByEmail(String email) {
        Optional<Employee> opt = repo.findByEmail(email);
        if (opt.isEmpty()) return false;
        repo.delete(opt.get());
        return true;
    }

    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    // --- Search: JPA Specifications ---
    public Optional<Employee> searchByEmailUsingSpec(String email) {
        if (email == null || email.isBlank()) return Optional.empty();
        Specification<Employee> spec = EmployeeSpecifications.hasEmail(email);
        return repo.findOne(spec);
    }

    public List<Employee> searchByNameUsingSpec(String name) {
        if (name == null || name.isBlank()) return Collections.emptyList();
        Specification<Employee> spec = EmployeeSpecifications.nameContains(name);
        return repo.findAll(spec);
    }

    // --- Search: HQL / JPQL ---
    public Optional<Employee> searchByEmailUsingHql(String email) {
        if (email == null || email.isBlank()) return Optional.empty();
        return repo.findByEmailHql(email);
    }

    public List<Employee> searchByNameUsingHql(String name) {
        if (name == null || name.isBlank()) return Collections.emptyList();
        return repo.findByNameHql(name);
    }

    // --- Search: Native SQL ---
    public Optional<Employee> searchByEmailUsingNative(String email) {
        if (email == null || email.isBlank()) return Optional.empty();
        return repo.findByEmailNative(email);
    }

    public List<Employee> searchByNameUsingNative(String name) {
        if (name == null || name.isBlank()) return Collections.emptyList();
        return repo.findByNameNative(name);
    }
}

