package com.dtechideas.restapitesting.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dtechideas.restapitesting.exception.ResourceAlreadyExistsException;
import com.dtechideas.restapitesting.exception.ResourceNotFoundException;
import com.dtechideas.restapitesting.model.Employee;
import com.dtechideas.restapitesting.repository.EmployeeRepository;
import com.dtechideas.restapitesting.service.impl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;
	@InjectMocks
	private EmployeeServiceImpl employeeService;

	Employee employee;

	@BeforeEach
	public void setUp() {
		employee = new Employee(1L,"Meenakshi", "Ramesh", "meenakshi.ramesh@dtechideas.com");
	}

	@AfterEach
	public void tearDown() {
		employee = null;
	}

	
	@Test
	@DisplayName("JUnit test for saveEmployee operation")
	public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
		// given - precondition or setup
		given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
		given(employeeRepository.save(employee)).willReturn(employee);
		// when - action or the behaviour
		Employee savedEmployee = employeeService.saveEmployee(employee);
		// then - verify the output
		assertThat(savedEmployee).isNotNull();

	}

	@Test
	@DisplayName("JUnit test for saveEmployee operation - ResourceAlreadyExists Exception")
	public void givenEmployeeObject_whenSaveEmployee_thenThrowsResourceAlreadyExistsException() {
		// given - precondition or setup
		given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
		// when - action or the behaviour
		assertThrows(ResourceAlreadyExistsException.class, () -> {
			employeeService.saveEmployee(employee);
		});
		// then - verify the output
		verify(employeeRepository, never()).save(any(Employee.class));
	}
	
	@Test
	@DisplayName("JUnit test for getAllEmployees operation")
	public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
		// given - precondition or setup
		Employee employee1 = new Employee("Dhandapani","Sudhakar","dhandapani.sudhakar@dtechideas.com");
		Employee employee2 = new Employee("Kathirvel","Sudhakar","kathirvel.sudhakar@dtechideas.com");
		given(employeeRepository.findAll()).willReturn(List.of(employee1,employee2));
		// when - action or the behaviour
		List<Employee> employees = employeeService.getAllEmployees();
		// then - verify the output
		assertThat(employees).isNotNull();
		assertThat(employees.size()).isEqualTo(2);
	}
	
	@Test
	@DisplayName("JUnit test for getAllEmployees operation - Empty List (Negative Scenario)")
	public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
		// given - precondition or setup
		given(employeeRepository.findAll()).willReturn(Collections.emptyList());
		// when - action or the behaviour
		List<Employee> employees = employeeService.getAllEmployees();
		// then - verify the output
		assertThat(employees).isEmpty();
		assertThat(employees.size()).isEqualTo(0);
	}
	
	@Test
	@DisplayName("JUnit test for getEmployeeById operation")
	public void givenEmployeeId_whenFindEmployeeById_thenReturnEmployee() {
		// given - precondition or setup
		given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
		// when - action or the behaviour
		Employee foundEmployee = employeeService.getEmployeeById(employee.getId());
		// then - verify the output
		assertThat(foundEmployee).isNotNull();
		assertThat(foundEmployee.getId()).isEqualTo(employee.getId());
	}
	
	@Test
	@DisplayName("JUnit test for getEmployeeById operation - throw ResourceNotFoundException")
	public void givenEmployeeId_whenFindEmployeeById_thenThrowResourceNotFoundException() {
		// given - precondition or setup
		given(employeeRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
		// when - action or the behaviour
		assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.getEmployeeById(employee.getId());
		});
	}
	
	@Test
	@DisplayName("JUnit test for updateEmployee operation")
	public void givenEmployee_whenUpdateEmployee_thenReturnEmployeeUpdated() {
		// given - precondition or setup
		given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
		employee.setEmail("meenakshi.r@dtechideas.com");
		given(employeeRepository.save(employee)).willReturn(employee);
		// when - action or the behaviour
		Employee updatedEmployee = employeeService.updateEmployee(employee.getId(),employee);
		// then - verify the output
		assertThat(updatedEmployee).isNotNull();
		assertThat(updatedEmployee.getEmail()).isEqualTo(employee.getEmail());
	}
	
	@Test
	@DisplayName("JUnit test for deleteEmployeeById operation")
	public void givenEmployeeId_whenDeleteEmployeeById_thenReturnTrue() {
		// given - precondition or setup
		given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
		willDoNothing().given(employeeRepository).deleteById(employee.getId());
		// when - action or the behaviour
		employeeService.deleteEmployeeById(employee.getId());
		// then - verify the output
		verify(employeeRepository,times(1)).deleteById(employee.getId());
	}
}
