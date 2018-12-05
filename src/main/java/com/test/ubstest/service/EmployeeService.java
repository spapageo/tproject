package com.test.ubstest.service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import com.test.ubstest.domain.Employee;

public interface EmployeeService {
	void saveOrUpdateByEmail(List<@Valid Employee> employees);

	void save(Employee employee);

	void update(Employee employee);

	List<Employee> getAll();

	Optional<Employee> getById(long id);

	void deleteById(long id);
}
