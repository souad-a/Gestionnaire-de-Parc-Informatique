package com.parcinformatique.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @OneToMany(mappedBy = "employee")
    private List<Assignment> assignments = new ArrayList<>();

    public Employee() {}

    public Employee(String firstName, String lastName, String department, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<Assignment> getAssignments() { return assignments; }
    public void setAssignments(List<Assignment> assignments) { this.assignments = assignments; }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + getFullName() + "', department='" + department + "'}";
    }
}