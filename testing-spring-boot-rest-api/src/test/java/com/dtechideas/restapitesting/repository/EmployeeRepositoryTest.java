package com.dtechideas.restapitesting.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dtechideas.restapitesting.model.Employee;

// By default, it will expect in-memory database (like H2) for testing, If we want to test against any other database like MySQL, use the following annotation: @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	private Employee employee;
	
	@BeforeEach
	public void setUp()
	{
		employee = new Employee("Dhandapani","Sudhakar","dhandapani.s@dtechideas.com");
	}

	// JUnit test to test save method
	@Test
	@DisplayName("JUnit test for save Employee operation")
	public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
		// given - Pre condition / setup
		/* Employee employee = new Employee("Dhandapani", "Sudhakar", "dhandapani.s@dtechideas.com"); */
		// when - actiona or behaviour
		Employee savedEmployee = employeeRepository.save(employee);
		// then - verify the output
		assertThat(savedEmployee).isNotNull();
		assertThat(savedEmployee.getId()).isGreaterThan(0);
	}

	// JUnit test for get all Employees operation
	@Test
	@DisplayName("JUnit test for get all Employees operation")
	public void givenEmployeesList_whenFindAll_thenReturnEmployeesList() {

		// given - precondition or setup
		Employee employee1 = new Employee("Aravinth", "Palanisamy", "aravinth.p@dtechideas.com");
		Employee employee2 = new Employee("Balamurugan", "Mani", "balamurugan.m@dtechideas.com");
		Employee employee3 = new Employee("Chandru", "Ravichandran", "chandru.r@dtechideas.com");
		employeeRepository.save(employee1);
		employeeRepository.save(employee2);
		employeeRepository.save(employee3);
		// when - action or the behaviour
		List<Employee> employees = employeeRepository.findAll();
		// then - verify the output
		assertThat(employees).isNotNull();
		assertThat(employees.size()).isEqualTo(3);
	}

	// JUnit test for get Employee by Id
	@Test
	@DisplayName("JUnit test for get Employee By Id operation")
	public void givenEmployee_whenFindById_thenReturnEmployee() {
		// given - precondition or setup
		/* Employee employee = new Employee("Nanthakumar", "Mohandoss", "nandhakumar.m@dtechideas.com"); */
		employeeRepository.save(employee);
		// when - action or the behaviour
		Employee foundEmployee = employeeRepository.findById(employee.getId()).get();
		// then - verify the output
		assertThat(foundEmployee).isNotNull();
		assertThat(foundEmployee.getId()).isEqualTo(employee.getId());
	}

	// JUnit test for get Employee by Email
	@Test
	@DisplayName("JUnit test for get Employee By Email operation")
	public void givenEmployee_whenFindByEmail_thenReturnEmployee() {
		// given - precondition or setup
		/* Employee employee = new Employee("Kannan", "Sundaralingam", "kannan.s@dtechideas.com"); */
		employeeRepository.save(employee);
		// when - action or the behaviour
		Employee foundEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
		// then - verify the output
		assertThat(foundEmployee).isNotNull();
		assertThat(foundEmployee.getEmail()).isEqualTo(employee.getEmail());
	}

	// JUnit test for update Employee
	@Test
	@DisplayName("JUnit test for update Employee operation")
	public void givenEmployee_whenUpdate_thenReturnEmployee() {
		// given - precondition or setup
		/* Employee employee = new Employee("Amallis Prajon", "Kathirvel", "amallisprajon.k@dtechideas.com"); */
		employeeRepository.save(employee);
		// when - action or the behaviour
		String newEmail = "dhandapani.sudhakar@dtechideas.com";
		Employee foundEmployee = employeeRepository.findById(employee.getId()).get();
		foundEmployee.setEmail(newEmail);
		Employee updatedEmployee = employeeRepository.save(foundEmployee);
		// then - verify the output
		assertThat(updatedEmployee).isNotNull();
		assertThat(updatedEmployee.getEmail()).isEqualTo(newEmail);
	}

	// JUnit test for delete Employee by Id
	@Test
	@DisplayName("JUnit test for delete Employee By Id operation")
	public void givenEmployee_whenDeleteById_thenRemoveEmployee() {
		// given - precondition or setup
		/* Employee employee = new Employee("Kathirvel", "Sudhakar", "kathirvel.sudhakar@dtechideas.com"); */
		Employee savedEmployee = employeeRepository.save(employee);
		// when - action or the behaviour
		employeeRepository.deleteById(savedEmployee.getId());
		Optional<Employee> optionalEmployee = employeeRepository.findById(savedEmployee.getId());
		// then - verify the output
		assertThat(optionalEmployee).isEmpty();
	}

	// JUnit test for custom query using JPQL with Index
	@Test
	@DisplayName("JUnit test for custom query using JPQL with Index")
	public void givenFirstNameLastName_whenFindByJPQLFirstNameLastName_thenReturnEmployee() {
		// given - precondition or setup
		/* Employee employee = new Employee("Prasanna", "Shivakumar", "prasanna.shivakumar@dtechideas.com"); */
		Employee savedEmployee = employeeRepository.save(employee);
		// when - action or the behaviour
		Employee foundEmployee = employeeRepository.findByJPQLFirstNameLastName(savedEmployee.getFirstName(),
				savedEmployee.getLastName());
		// then - verify the output
		assertThat(foundEmployee).isNotNull();
		assertThat(foundEmployee.getFirstName()).isEqualTo(savedEmployee.getFirstName());
		assertThat(foundEmployee.getLastName()).isEqualTo(savedEmployee.getLastName());
	}

	// JUnit test for custom query using JPQL with Named Parameters
	@Test
	@DisplayName("JUnit test for custom query using JPQL with Named Parameters")
	public void givenFirstNameLastName_whenFindByJPQLNamedParamsFirstNameLastName_thenReturnEmployee() {
		// given - precondition or setup
		Employee employee = new Employee("Prasanna", "Shiva", "prasanna.shiva@dtechideas.com");
		Employee savedEmployee = employeeRepository.save(employee);
		// when - action or the behaviour
		Employee foundEmployee = employeeRepository.findByJPQLNamedParamsFirstNameLastName(savedEmployee.getFirstName(),
				savedEmployee.getLastName());
		// then - verify the output
		assertThat(foundEmployee).isNotNull();
		assertThat(foundEmployee.getFirstName()).isEqualTo(savedEmployee.getFirstName());
		assertThat(foundEmployee.getLastName()).isEqualTo(savedEmployee.getLastName());
	}
	
	// JUnit test for custom query using Native SQL with Index
		@Test
		@DisplayName("JUnit test for custom query using Native SQL with Index")
		public void givenFirstNameLastName_whenFindByNativeSQLFirstNameLastName_thenReturnEmployee() {
			// given - precondition or setup
			/* Employee employee = new Employee("Sudhakar", "Sakthivel", "sudhakar.sakthivel@dtechideas.com"); */
			Employee savedEmployee = employeeRepository.save(employee);
			// when - action or the behaviour
			Employee foundEmployee = employeeRepository.findByNativeSQLFirstNameLastName(savedEmployee.getFirstName(),
					savedEmployee.getLastName());
			// then - verify the output
			assertThat(foundEmployee).isNotNull();
			assertThat(foundEmployee.getFirstName()).isEqualTo(savedEmployee.getFirstName());
			assertThat(foundEmployee.getLastName()).isEqualTo(savedEmployee.getLastName());
		}

		// JUnit test for custom query using Native SQL with Named Parameters
		@Test
		@DisplayName("JUnit test for custom query using Native SQL with Named Parameters")
		public void givenFirstNameLastName_whenFindByNativeSQLNamedParamsFirstNameLastName_thenReturnEmployee() {
			// given - precondition or setup
			/* Employee employee = new Employee("Arunthathi", "Sudhakar", "arunthathi.sudhakar@dtechideas.com"); */
			Employee savedEmployee = employeeRepository.save(employee);
			// when - action or the behaviour
			Employee foundEmployee = employeeRepository.findByNativeSQLNamedParamsFirstNameLastName(savedEmployee.getFirstName(),
					savedEmployee.getLastName());
			// then - verify the output
			assertThat(foundEmployee).isNotNull();
			assertThat(foundEmployee.getFirstName()).isEqualTo(savedEmployee.getFirstName());
			assertThat(foundEmployee.getLastName()).isEqualTo(savedEmployee.getLastName());
		}
		
		@AfterEach
		public void tearDown()
		{
			employee = null;
		}

}
