package com.test.ubstest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;

import com.test.ubstest.domain.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeCsvLoaderServiceImplTest {

	private EmployeeCsvLoaderService employeeCsvLoaderService;

	@Mock
	private EmployeeService employeeService;

	@Captor
	private ArgumentCaptor<List<Employee>> argumentCaptor;

	@Before
	public void setUp() throws Exception {
		File csvFile = new File(this.getClass().getResource("/com/test/ubstest/service/test.csv").toURI());
		employeeCsvLoaderService = new EmployeeCsvLoaderServiceImpl(csvFile, employeeService);
	}

	@Test
	public void loadCsv_convertsAndCallsService() {
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

		employeeCsvLoaderService.loadCsv();

		verify(employeeService).saveOrUpdateByEmail(argumentCaptor.capture());

		List<Employee> savedEmployees = argumentCaptor.getValue();

		assertThat(savedEmployees).hasSize(1);

		assertThat(savedEmployees.get(0)).isEqualToComparingFieldByField(employee);
	}
}