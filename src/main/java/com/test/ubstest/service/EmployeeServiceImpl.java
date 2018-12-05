package com.test.ubstest.service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import com.test.ubstest.domain.Employee;
import com.test.ubstest.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public void saveOrUpdateByEmail(List<@Valid Employee> employees) {
		employees.stream() //
						.map(this::updateIfExistingByEmail) //
						.forEach(employeeRepository::save);
	}

	@Override
	public void save(Employee employee) {
		employeeRepository.save(employee);
	}

	@Override
	public void update(Employee employee) {
		Employee employeeToModify = employeeRepository.findById(employee.getId()).orElseThrow(IllegalArgumentException::new);

		copy(employee, employeeToModify);

		employeeRepository.save(employeeToModify);
	}

	@Override
	public List<Employee> getAll() {
		return employeeRepository.findAll();
	}

	@Override
	public Optional<Employee> getById(long id) {
		return employeeRepository.findById(id);
	}

	@Override
	public void deleteById(long id) {
		employeeRepository.deleteById(id);
	}

	private Employee updateIfExistingByEmail(Employee employee) {
		Employee employeeToModify = employeeRepository.findByEmail(employee.getEmail())
						.orElse(new Employee());

		copy(employee, employeeToModify);

		return employeeToModify;
	}

	private void copy(Employee source, Employee dest) {
		dest.setWeb(source.getWeb());
		dest.setPostal(source.getPostal());
		dest.setPhone1(source.getPhone1());
		dest.setPhone2(source.getPhone2());
		dest.setLastName(source.getLastName());
		dest.setFirstName(source.getFirstName());
		dest.setEmail(source.getEmail());
		dest.setCity(source.getCity());
		dest.setCountry(source.getCountry());
		dest.setCompanyName(source.getCompanyName());
		dest.setAddress(source.getAddress());
	}
}
