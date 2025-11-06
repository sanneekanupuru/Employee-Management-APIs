package com.example.employeeapi.controller;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import com.example.employeeapi.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;
    private final EmployeeRepository repo;

    public EmployeeController(EmployeeService service, EmployeeRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    // 1. GET /employees/email/{email} - Fetch by email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByEmail(@PathVariable String email) {
        Optional<Employee> emp = service.findByEmail(email);
        return emp.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 2. GET /employees/name/{name} - Fetch by name (first or last)
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        List<Employee> list = service.findByName(name);
        if (list.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(list);
    }

    // 3. POST /employees - Create (Name and Email required)
    @PostMapping
    public ResponseEntity<?> createBasic(@RequestBody Map<String, String> body) {
        String firstName = body.get("firstName");
        String email = body.get("email");
        if (firstName == null || firstName.isBlank() || email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("firstName and email are required");
        }
        if (service.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Employee with this email already exists");
        }
        Employee e = new Employee();
        e.setFirstName(firstName);
        e.setLastName(body.get("lastName"));
        e.setEmail(email);
        Employee saved = service.create(e);
        return ResponseEntity.created(URI.create("/employees/email/" + saved.getEmail())).body(saved);
    }

    // 4. POST /employees/details - Create (Name, Email, Phone required)
    @PostMapping("/details")
    public ResponseEntity<?> createDetails(@RequestBody Map<String, String> body) {
        String firstName = body.get("firstName");
        String email = body.get("email");
        String phone = body.get("phone");
        if (firstName == null || firstName.isBlank() ||
                email == null || email.isBlank() ||
                phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body("firstName, email and phone are required");
        }
        if (service.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Employee with this email already exists");
        }
        Employee e = new Employee();
        e.setFirstName(firstName);
        e.setLastName(body.get("lastName"));
        e.setEmail(email);
        e.setPhone(phone);
        Employee saved = service.create(e);
        return ResponseEntity.created(URI.create("/employees/email/" + saved.getEmail())).body(saved);
    }

    // 5. PUT /employees/update  - Update lastName, phone, address. Identify by email in body.
    @PutMapping("/update")
    public ResponseEntity<?> updateDetails(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("email is required to identify the employee");
        }
        String lastName = body.get("lastName");
        String phone = body.get("phone");
        String address = body.get("address");

        Employee updated = service.updateDetailsByEmail(email, lastName, phone, address);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // 6. PATCH /employees/phone/{email} - Update phone only
    @PatchMapping("/phone/{email}")
    public ResponseEntity<?> updatePhone(@PathVariable String email, @RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body("phone is required");
        }
        Employee updated = service.updatePhoneByEmail(email, phone);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // 7. DELETE /employees/email/{email} - Delete by email
    @DeleteMapping("/email/{email}")
    public ResponseEntity<?> deleteByEmail(@PathVariable String email) {
        boolean removed = service.deleteByEmail(email);
        if (!removed) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    // Search - Specifications
    @GetMapping("/search/spec/email/{email}")
    public ResponseEntity<?> getByEmailSpec(@PathVariable String email) {
        if (email == null || email.isBlank()) return ResponseEntity.badRequest().body("email required");
        return service.searchByEmailUsingSpec(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/spec/name/{name}")
    public ResponseEntity<?> getByNameSpec(@PathVariable String name) {
        if (name == null || name.isBlank()) return ResponseEntity.badRequest().body("name required");
        List<Employee> list = service.searchByNameUsingSpec(name);
        if (list.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(list);
    }

    // Search - HQL
    @GetMapping("/search/hql/email/{email}")
    public ResponseEntity<?> getByEmailHql(@PathVariable String email) {
        if (email == null || email.isBlank()) return ResponseEntity.badRequest().body("email required");
        return service.searchByEmailUsingHql(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/hql/name/{name}")
    public ResponseEntity<?> getByNameHql(@PathVariable String name) {
        if (name == null || name.isBlank()) return ResponseEntity.badRequest().body("name required");
        List<Employee> list = service.searchByNameUsingHql(name);
        if (list.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(list);
    }

    // Search - Native
    @GetMapping("/search/native/email/{email}")
    public ResponseEntity<?> getByEmailNative(@PathVariable String email) {
        if (email == null || email.isBlank()) return ResponseEntity.badRequest().body("email required");
        return service.searchByEmailUsingNative(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/native/name/{name}")
    public ResponseEntity<?> getByNameNative(@PathVariable String name) {
        if (name == null || name.isBlank()) return ResponseEntity.badRequest().body("name required");
        List<Employee> list = service.searchByNameUsingNative(name);
        if (list.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(list);
    }
}
