package com.dtechideas.restapitesting.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.dtechideas.restapitesting.exception.ResourceNotFoundException;
import com.dtechideas.restapitesting.model.Employee;
import com.dtechideas.restapitesting.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@WebMvcTest
public class EmployeeControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;
	/*
	 * @MockBean annotation tells Spring to create mock instance of EmployeeService
	 * and add it to the application context so that it is injected to the
	 * EmployeeController.
	 */
	@MockBean
	private EmployeeService employeeService;

	private Employee employee;

	@BeforeEach
	public void setUp() {
		employee = new Employee(1L, "Dhandapani", "Sudhakar", "dhandapani.sudhakar@dtechideas.com");
	}
	
	@AfterEach
	public void tearDown()
	{
		employee = null;
	}

	@Test
	@DisplayName("JUnit test for createEmployee operation")
	public void givenEmployee_whenCreateEmployee_thenReturnEmployee() {
		try {
			// given - precondition or setup
			given(employeeService.saveEmployee(any(Employee.class))).willReturn(employee);
			// when - action or the behaviour
			ResultActions response = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsBytes(employee)));
			// then - verify the output
			response.andDo(print());
			response.andDo(print()).andExpect(status().isCreated())
					.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
					.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
					.andExpect(jsonPath("$.email", is(employee.getEmail())));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Test
	@DisplayName("JUnit test for getAllEmployees operation")
	public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
		// given - precondition or setup
		Employee employee1 = new Employee(1L,"Richard", "Parker", "richard.parker@dtechideas.com");
		Employee employee2 = new Employee(2L,"Peter", "Parker", "peter.parker@dtechideas.com");
		List<Employee> employees = List.of(employee1, employee2);
		given(employeeService.getAllEmployees()).willReturn(employees);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(get("/api/v1/employees"));
		// then - verify the output
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(employees.size())));
	}
	
	@Test
	@DisplayName("JUnit test for getEmployeeById operation")
	public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
		// given - precondition or setup 
		given(employeeService.getEmployeeById(employee.getId())).willReturn(employee);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", employee.getId()));
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
		given(employeeService.getEmployeeById(employee.getId())).willThrow(ResourceNotFoundException.class);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", employee.getId()));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("JUnit test for updateEmployee operation")
	public void givenEmployeeWithUpdates_whenUpdatedEmployee_thenReturnEmployeeUpdated() throws JsonProcessingException, Exception {
		// given - precondition or setup
		Employee employeeForUpdate = new Employee(employee.getId(),"Sudhakar","Dhandapani","sudhakar.dhandapani@outlook.com");
		given(employeeService.updateEmployee(anyLong(), any(Employee.class))).willReturn(employeeForUpdate);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}", employee.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(employeeForUpdate)));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName",is(employeeForUpdate.getFirstName()))) 
		.andExpect(jsonPath("$.lastName",is(employeeForUpdate.getLastName())))
		.andExpect(jsonPath("$.email",is(employeeForUpdate.getEmail())));
	}
	
	@Test
	@DisplayName("JUnit test for updateEmployee operation - ResourceNotFoundException")
	public void givenInvalidEmployeeWithUpdates_whenUpdatedEmployee_thenThrowsResourceNotFoundException() throws JsonProcessingException, Exception {
		// given - precondition or setup
		Employee employeeForUpdate = new Employee(employee.getId(),"Sudhakar","Dhandapani","sudhakar.dhandapani@outlook.com");
		given(employeeService.updateEmployee(anyLong(), any(Employee.class))).willThrow(ResourceNotFoundException.class);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}", employee.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(employeeForUpdate)));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("JUnit test for deleteEmployeeById operation")
	public void givenEmployeeId_whenDeleteEmployeeById_thenReturnTrue()throws Exception {
		// given - precondition or setup
		willDoNothing().given(employeeService).deleteEmployeeById(employee.getId());
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(delete("/api/v1/employees/{id}", employee.getId()));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("JUnit test for deleteEmployeeById operation - ResourceNotFoundException")
	public void givenInvalidEmployeeId_whenDeleteEmployeeById_thenThrowsResourceNotFoundException() throws Exception {
		// given - precondition or setup
		willThrow(ResourceNotFoundException.class).given(employeeService).deleteEmployeeById(employee.getId());		
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(delete("/api/v1/employees/{id}", employee.getId()));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isNotFound());
	}
}
