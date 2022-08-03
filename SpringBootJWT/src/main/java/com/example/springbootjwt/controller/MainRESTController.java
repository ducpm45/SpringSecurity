package com.example.springbootjwt.controller;

import com.example.springbootjwt.dao.EmployeeDAO;
import com.example.springbootjwt.model.Employee;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainRESTController {
    private final EmployeeDAO employeeDAO;

    public MainRESTController(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @RequestMapping("/")
    @ResponseBody
    public String welcome() {
        return "Welcome to Spring Boot + REST + JWT Example.";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "{greeting: 'Hello'}";
    }

    // URL:
    // http://localhost:8080/employees
    @GetMapping(value = "/employees", produces = {MediaType.APPLICATION_JSON_VALUE, //
            MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public List<Employee> getEmployees() {
        return employeeDAO.getAllEmployees();
    }

    // URL:
    // http://localhost:8080/employee/{empNo}
    @GetMapping(value = "/employee/{empNo}", produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Employee getEmployee(@PathVariable("empNo") String empNo) {
        return employeeDAO.getEmployee(empNo);
    }

    // URL:
    // http://localhost:8080/employee

    @PostMapping(value = "/employee", produces = {MediaType.APPLICATION_JSON_VALUE, //
            MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Employee addEmployee(@RequestBody Employee emp) {

        System.out.println("(Service Side) Creating employee: " + emp.getEmpNo());

        return employeeDAO.addEmployee(emp);
    }

    // URL:
    // http://localhost:8080/employee
    @PutMapping(value = "/employee", produces = {MediaType.APPLICATION_JSON_VALUE, //
            MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Employee updateEmployee(@RequestBody Employee emp) {

        System.out.println("(Service Side) Editing employee: " + emp.getEmpNo());

        return employeeDAO.updateEmployee(emp);
    }

    // URL:
    // http://localhost:8080/employee/{empNo}
    @DeleteMapping(value = "/employee/{empNo}", produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public void deleteEmployee(@PathVariable("empNo") String empNo) {

        System.out.println("(Service Side) Deleting employee: " + empNo);

        employeeDAO.deleteEmployee(empNo);
    }
}
