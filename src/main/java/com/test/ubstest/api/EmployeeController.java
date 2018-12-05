package com.test.ubstest.api;

import java.util.List;

import com.google.common.base.Preconditions;
import com.test.ubstest.domain.Employee;
import com.test.ubstest.service.EmployeeService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping("/api/employees")
	List<Employee> getAll() {
		return employeeService.getAll();
	}

	@GetMapping("/api/employees/{id}")
	Employee getById(@PathVariable("id") long id) {
		return employeeService.getById(id).orElseThrow(EmployeeNotFoundException::new);
	}


	@PostMapping("/api/employees")
	void create(Employee employee) {
		employeeService.save(employee);
	}

	@PutMapping("/api/employees/{id}")
	void update(Employee employee, @PathVariable("id") long id) {
		Preconditions.checkArgument(employee.getId() == id);
		if(!employeeService.getById(id).isPresent()) {
			throw new EmployeeNotFoundException();
		}
		employeeService.update(employee);
	}

	@DeleteMapping("/api/employees/{id}")
	void delete(@PathVariable("id") long id) {
		if(!employeeService.getById(id).isPresent()) {
			throw new EmployeeNotFoundException();
		}
		employeeService.deleteById(id);
	}
}
