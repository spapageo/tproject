package com.test.ubstest.service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.test.ubstest.domain.Employee;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

enum Header {
	first_name, last_name, company_name, address, city, county, postal, phone1, phone2, email, web
}

@Service
@Transactional
public class EmployeeCsvLoaderServiceImpl implements EmployeeCsvLoaderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeCsvLoaderServiceImpl.class);

	private final File csvFile;
	private final EmployeeService employeeService;

	@Autowired
	public EmployeeCsvLoaderServiceImpl(@Value("${employee.file}") String csvFile, EmployeeService employeeService) {
		this(Paths.get(csvFile).toFile(), employeeService);
	}

	public EmployeeCsvLoaderServiceImpl(File csvFile, EmployeeService employeeService) {
		this.csvFile = csvFile;
		this.employeeService = employeeService;
		Preconditions.checkArgument(this.csvFile.isFile());
		Preconditions.checkArgument(this.csvFile.canRead());
	}

	@Override
	@Scheduled(fixedDelayString = "P1D", initialDelay = 1000)
	public void loadCsv() {

		try (Reader fileReader = new FileReader(csvFile); CSVParser csvParser = CSVFormat.DEFAULT
						.withRecordSeparator('\n').withHeader(Header.class).withSkipHeaderRecord().parse(fileReader)) {
			List<Employee> employees = new ArrayList<>();
			for (CSVRecord csvRecord : csvParser) {
				LOGGER.info("Loading record {}", csvRecord);
				employees.add(createEmployee(csvRecord));
			}

			employeeService.saveOrUpdateByEmail(employees);
		} catch (IOException e) {
			LOGGER.warn("Error parsing csv file");
		}

	}

	private Employee createEmployee(CSVRecord csvRecord) {
		Employee employee = new Employee();
		employee.setAddress(csvRecord.get(Header.address));
		employee.setCity(csvRecord.get(Header.city));
		employee.setCompanyName(csvRecord.get(Header.company_name));
		employee.setCountry(csvRecord.get(Header.county));
		employee.setEmail(csvRecord.get(Header.email));
		employee.setFirstName(csvRecord.get(Header.first_name));
		employee.setLastName(csvRecord.get(Header.last_name));
		employee.setPhone1(csvRecord.get(Header.phone1));
		employee.setPhone2(csvRecord.get(Header.phone2));
		employee.setPostal(csvRecord.get(Header.postal));
		employee.setWeb(csvRecord.get(Header.web));
		return employee;
	}
}
