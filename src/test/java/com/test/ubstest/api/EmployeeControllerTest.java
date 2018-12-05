package com.test.ubstest.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.test.ubstest.domain.Employee;
import com.test.ubstest.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {

	@Mock
	private EmployeeService employeeService;

	@InjectMocks
	private EmployeeController employeeController;

	@Test
	public void getAll_callsService() {
		List<Employee> employees = Collections.singletonList(mock(Employee.class));
		when(employeeService.getAll()).thenReturn(employees);

		assertThat(employeeController.getAll()).isEqualTo(employees);

		verify(employeeService).getAll();
	}

	@Test(expected = EmployeeNotFoundException.class)
	public void getById_whenNotExists_throws() {
		when(employeeService.getById(1L)).thenReturn(Optional.empty());
		employeeController.getById(1L);
	}

	@Test
	public void getById_whenExists_returns() {
		Employee employee = mock(Employee.class);
		when(employeeService.getById(1L)).thenReturn(Optional.of(employee));

		assertThat(employeeController.getById(1L)).isEqualTo(employee);

		verify(employeeService).getById(1L);
	}

	@Test
	public void create_callsService() {
		Employee employee = mock(Employee.class);
		employeeController.create(employee);

		verify(employeeService).save(employee);
	}

	@Test(expected = EmployeeNotFoundException.class)
	public void update_whenNotExists_throws() {
		Employee employee = mock(Employee.class);
		when(employee.getId()).thenReturn(1L);
		employeeController.update(employee, 1L);
	}

	@Test
	public void update_whenExists_callsService() {
		Employee employee = mock(Employee.class);
		when(employee.getId()).thenReturn(1L);

		when(employeeService.getById(1L)).thenReturn(Optional.of(mock(Employee.class)));

		employeeController.update(employee, 1L);

		verify(employeeService).getById(1L);
		verify(employeeService).update(employee);
	}

	@Test
	public void delete_whenExists_callsService() {
		when(employeeService.getById(1L)).thenReturn(Optional.of(mock(Employee.class)));

		employeeController.delete(1L);

		verify(employeeService).deleteById(1L);
	}


	@Test(expected = EmployeeNotFoundException.class)
	public void delete_whenNotExists_throws() {
		employeeController.delete(1L);
	}
}