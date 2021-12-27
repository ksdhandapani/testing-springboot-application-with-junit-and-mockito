package com.dtechideas.restapitesting.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dtechideas.restapitesting.exception.ResourceAlreadyExistsException;
import com.dtechideas.restapitesting.exception.ResourceNotFoundException;
import com.dtechideas.restapitesting.model.Employee;
import com.dtechideas.restapitesting.repository.EmployeeRepository;
import com.dtechideas.restapitesting.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository;

	/* When we have a single constructor, we do not have to use @Autowired */
	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Employee saveEmployee(Employee employee) {
		Optional<Employee> optionalEmployee = this.employeeRepository.findByEmail(employee.getEmail());
		if (optionalEmployee.isPresent()) {
			throw new ResourceAlreadyExistsException("Employee", "email", employee.getEmail());
		}
		Employee savedEmployee = this.employeeRepository.save(employee);
		return savedEmployee;
	}

	@Override
	public List<Employee> getAllEmployees() {
		return this.employeeRepository.findAll();
	}

	@Override
	public Employee getEmployeeById(Long id) {
		Employee employee = this.employeeRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Employee","id",id));
		return employee;
	}

	@Override
	public Employee updateEmployee(Long employeeId, Employee employee) {
		Employee foundEmployee = this.employeeRepository.findById(employeeId).orElseThrow( () -> new ResourceNotFoundException("Employee","id",employeeId));
		foundEmployee.setFirstName(employee.getFirstName());
		foundEmployee.setLastName(employee.getLastName());
		foundEmployee.setEmail(employee.getEmail());
		Employee updatedEmployee = employeeRepository.save(foundEmployee);
		return updatedEmployee;
	}

	@Override
	public void deleteEmployeeById(Long id) {
		Employee foundEmployee = this.employeeRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Employee","id",id));
		this.employeeRepository.deleteById(foundEmployee.getId());
	}

}
