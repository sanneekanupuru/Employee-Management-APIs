package com.example.employeeapi.repository;

import com.example.employeeapi.model.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecifications {

    public static Specification<Employee> hasEmail(String email) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("email")), email.toLowerCase());
    }

    public static Specification<Employee> nameContains(String name) {
        String like = "%" + name.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("firstName")), like),
                cb.like(cb.lower(root.get("lastName")), like)
        );
    }
}
