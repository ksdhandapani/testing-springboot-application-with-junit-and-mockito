package com.dtechideas.restapitesting.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.dtechideas.restapitesting.exception.ResourceNotFoundException;
import com.dtechideas.restapitesting.model.Employee;
import com.dtechideas.restapitesting.repository.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		this.employeeRepository.deleteAll();
	}

	@Test
	@DisplayName("JUnit test for createEmployee operation")
	public void givenEmployee_whenCreateEmployee_thenReturnEmployee() throws JsonProcessingException, Exception {

		// given - precondition or setup
		Employee employee = new Employee(1L, "Richard", "Parker", "richard.parkar@dtechideas.com");
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsBytes(employee)));
		// then - verify the output
		response.andDo(print());
		response.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}
	
	@Test
	@DisplayName("JUnit test for getAllEmployees operation")
	public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
		// given - precondition or setup
		Employee employee1 = new Employee(1L,"Richard", "Parker", "richard.parker@dtechideas.com");
		Employee employee2 = new Employee(2L,"Peter", "Parker", "peter.parker@dtechideas.com");
		List<Employee> employees = List.of(employee1, employee2);
		this.employeeRepository.saveAll(employees);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(get("/api/v1/employees"));
		// then - verify the output
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(employees.size())));
	}
	
	@Test
	@DisplayName("JUnit test for getEmployeeById operation")
	public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
		// given - precondition or setup 
		Employee employee = new Employee("Peter","Parker","peter.parker@dtechideas.com");
		Employee savedEmployee = this.employeeRepository.save(employee);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", savedEmployee.getId()));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
		.andExpect(jsonPath("$.email",is(employee.getEmail())));
		
	}
	
	@Test
	@DisplayName("JUnit test for getEmployeeById operation - ResourceNotFoundException")
	public void givenInvalidEmployeeId_whenGetEmployeeById_thenThrowsResourceNotFoundException() throws Exception {
		// given - precondition or setup 
		this.employeeRepository.findById(0L);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", 0L));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("JUnit test for updateEmployee operation")
	public void givenEmployeeWithUpdates_whenUpdatedEmployee_thenReturnEmployeeUpdated() throws JsonProcessingException, Exception {
		// given - precondition or setup
		Employee employeeForSave = new Employee("Dhandapani","Sudhakar","dhandapani.sudhakar@outlook.com");
		Employee employeeToBeUpdated = this.employeeRepository.save(employeeForSave);
		employeeToBeUpdated.setFirstName("Sudhakar");
		employeeToBeUpdated.setLastName("Dhandapani");
		employeeToBeUpdated.setEmail("sudhakar.dhandapani@outlook.com");
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}", employeeToBeUpdated.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(employeeToBeUpdated)));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName",is(employeeToBeUpdated.getFirstName()))) 
		.andExpect(jsonPath("$.lastName",is(employeeToBeUpdated.getLastName())))
		.andExpect(jsonPath("$.email",is(employeeToBeUpdated.getEmail())));
	}
	
	@Test
	@DisplayName("JUnit test for updateEmployee operation - ResourceNotFoundException")
	public void givenInvalidEmployeeWithUpdates_whenUpdatedEmployee_thenThrowsResourceNotFoundException() throws JsonProcessingException, Exception {
		// given - precondition or setup
		Long invalidEmployeeId = 0L;
		Employee employeeForUpdate = new Employee("Dhandapani","Sudhakar","dhandapani.sudhakar@outlook.com");
		Employee employeeToBeUpdated = this.employeeRepository.save(employeeForUpdate);
		employeeToBeUpdated.setFirstName("Sudhakar");
		employeeToBeUpdated.setLastName("Dhandapani");
		employeeToBeUpdated.setEmail("sudhakar.dhandapani@outlook.com");
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}", invalidEmployeeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(employeeToBeUpdated)));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("JUnit test for deleteEmployeeById operation")
	public void givenEmployeeId_whenDeleteEmployeeById_thenReturnTrue()throws Exception {
		// given - precondition or setup
		Employee employeeTobeSaved = new Employee("Dhandapani","Sudhakar","dhandapani.sudhakar@outlook.com");
		Employee employeeSaved = this.employeeRepository.save(employeeTobeSaved);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(delete("/api/v1/employees/{id}", employeeSaved.getId()));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("JUnit test for deleteEmployeeById operation - ResourceNotFoundException")
	public void givenInvalidEmployeeId_whenDeleteEmployeeById_thenThrowsResourceNotFoundException() throws Exception {
		// given - precondition or setup
		Long invalidEmployeeId = 0L;
		Employee employeeTobeSaved = new Employee("Dhandapani","Sudhakar","dhandapani.sudhakar@outlook.com");
		this.employeeRepository.save(employeeTobeSaved);	
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(delete("/api/v1/employees/{id}", invalidEmployeeId));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isNotFound());
	}
	
	@AfterEach
	public void tearDown() {

	}
}
