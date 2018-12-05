package com.test.ubstest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.test.ubstest.domain.Employee;
import com.test.ubstest.repository.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeeServiceImpl employeeService;

	@Captor
	private ArgumentCaptor<Employee> argumentCaptor;

	@Test
	public void saveOrUpdateByEmail_whenDoesNotExist_saves() {
		Employee employee = new Employee();
		employee.setAddress("address");
		employee.setCompanyName("companyName");
		employee.setCity("city");
		employee.setEmail("email");
		employee.setFirstName("firstName");
		employee.setLastName("lastName");
		employee.setPhone1("phone1");
		employee.setPhone2("phone2");
		employee.setPostal("postal");
		employee.setWeb("web");

		employeeService.saveOrUpdateByEmail(Collections.singletonList(employee));


		verify(employeeRepository).findByEmail(employee.getEmail());
		verify(employeeRepository).save(argumentCaptor.capture());

		Employee savedEmployee = argumentCaptor.getValue();

		assertThat(savedEmployee).isEqualToComparingFieldByField(employee);
	}

	@Test
	public void saveOrUpdateByEmail_whenExist_updated() {
		Employee employee = new Employee();
		employee.setAddress("address");
		employee.setCompanyName("companyName");
		employee.setCity("city");
		employee.setCountry("country");
		employee.setEmail("email");
		employee.setFirstName("firstName");
		employee.setLastName("lastName");
		employee.setPhone1("phone1");
		employee.setPhone2("phone2");
		employee.setPostal("postal");
		employee.setWeb("web");

		Employee savedEmployee = new Employee();

		when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(savedEmployee));

		employeeService.saveOrUpdateByEmail(Collections.singletonList(employee));

		verify(employeeRepository).findByEmail(employee.getEmail());
		verify(employeeRepository).save(argumentCaptor.capture());

		assertThat(argumentCaptor.getValue()).isEqualToComparingFieldByField(employee);
	}

	@Test
	public void save_forwardsCallToRepo() {
		Employee employee = mock(Employee.class);
		employeeService.save(employee);


		verify(employeeRepository).save(employee);
	}

	@Test
	public void getAll_forwardsCallToRepo() {
		List<Employee> employees = Collections.singletonList(mock(Employee.class));

		when(employeeRepository.findAll()).thenReturn(employees);

		assertThat(employeeService.getAll()).isEqualTo(employees);
		verify(employeeRepository).findAll();
	}

	@Test
	public void getById_forwardsCallToRepo() {
		Employee employee = mock(Employee.class);
		when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

		assertThat(employeeService.getById(1L)).isEqualTo(Optional.of(employee));
		verify(employeeRepository).findById(1L);
	}

	@Test
	public void deleteById_forwardsCallToRepo() {
		employeeService.deleteById(1L);

		verify(employeeRepository).deleteById(1L);
	}


	@Test
	public void update_whenExist_updated() {
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setAddress("address");
		employee.setCompanyName("companyName");
		employee.setCity("city");
		employee.setCountry("country");
		employee.setEmail("email");
		employee.setFirstName("firstName");
		employee.setLastName("lastName");
		employee.setPhone1("phone1");
		employee.setPhone2("phone2");
		employee.setPostal("postal");
		employee.setWeb("web");

		Employee savedEmployee = new Employee();
		savedEmployee.setId(1L);

		when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(savedEmployee));

		employeeService.update(employee);

		verify(employeeRepository).findById(employee.getId());
		verify(employeeRepository).save(argumentCaptor.capture());

		assertThat(argumentCaptor.getValue()).isEqualToComparingFieldByField(employee);
	}

	@Test(expected = IllegalArgumentException.class)
	public void update_whenNotExist_throws() {
		Employee employee = new Employee();
		employee.setId(1L);

		employeeService.update(employee);
	}
}