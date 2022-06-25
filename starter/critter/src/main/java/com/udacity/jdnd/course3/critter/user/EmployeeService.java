package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee findEmployeeById(long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return employee;
        } else {
            throw new EmployeeNotFoundException("Employee Not Found");
        }
    }

    public void setDaysAvailable(Set<DayOfWeek> daysAvailable, Long id){
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setDaysAvailable(daysAvailable);

        } else {
            throw new EmployeeNotFoundException("Employee Not Found");
        }
    }

    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    List<Employee> findByDaysAvailableContainsAndSkillsIn(DayOfWeek day, Set<EmployeeSkill> skills) {
        return employeeRepository.findByDaysAvailableContainsAndSkillsIn(day, skills);
    }
}
