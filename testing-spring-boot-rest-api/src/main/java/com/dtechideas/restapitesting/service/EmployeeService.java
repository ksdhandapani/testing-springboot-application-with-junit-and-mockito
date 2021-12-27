package com.dtechideas.restapitesting.service;


import java.util.List;

import com.dtechideas.restapitesting.model.Employee;

public interface EmployeeService {
	public Employee saveEmployee(Employee employee);
	public List<Employee> getAllEmployees();
	public Employee getEmployeeById(Long id);
	public Employee updateEmployee(Long employeeId, Employee employee);
	public void deleteEmployeeById(Long id);
}
