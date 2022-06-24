package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.*;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
//@Transactional
public class UserController {

    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final PetService petService;

    public UserController(EmployeeService employeeService, CustomerService customerService, PetService petService) {
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.petService = petService;
    }

    public EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    public Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO) {
        Employee employee  = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

    public List<EmployeeDTO> convertListEmployeeToListEmployeeDTO(List<Employee> employees) {
        List<EmployeeDTO> result = new ArrayList<>();
        for (Employee employee: employees) {
            result.add(convertEmployeeToEmployeeDTO(employee));
        }
        return result;
    }

    public CustomerDTO getCustomer(long id){
        return convertCustomerToCustomerDTO(customerService.findCustomerById(id));
    }

    public CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        List<Long> petIds = new ArrayList<>();
        if(customer.getPets() != null){
            for(Pet pet : customer.getPets()){
                petIds.add(pet.getId());
            }
        }
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }

    public Customer convertCustomerDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer  = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        List<Pet> pets = new ArrayList<>();
        if(customerDTO.getPetIds() != null){
            for(Long petId : customerDTO.getPetIds()){
                pets.add(petService.findPetById(petId));
            }
        }
        customer.setPets(pets);
        return customer;
    }

    public List<CustomerDTO> convertListCustomerToListCustomerDTO(List<Customer> customers) {
        List<CustomerDTO> result = new ArrayList<>();
        for (Customer customer: customers) {
            result.add(convertCustomerToCustomerDTO(customer));
        }
        return result;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return convertCustomerToCustomerDTO(customerService.saveCustomer(convertCustomerDTOToCustomer(customerDTO)));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return convertListCustomerToListCustomerDTO(customerService.findAllCustomers());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Pet pet = petService.findPetById(petId);
        return convertCustomerToCustomerDTO(pet.getCustomer());
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return convertEmployeeToEmployeeDTO(employeeService.saveEmployee(convertEmployeeDTOToEmployee(employeeDTO)));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEmployeeToEmployeeDTO(employeeService.findEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setDaysAvailable(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employeesAvailable = employeeService.findByDaysAvailableContainsAndSkillsIn(
                employeeDTO.getDate().getDayOfWeek(), employeeDTO.getSkills());
        HashSet<EmployeeSkill> skillsToCover = new HashSet<>(employeeDTO.getSkills());
        if (skillsToCover.size() > 0) {
            List<EmployeeDTO> employeeNeeded = new ArrayList<>();
//            for (Employee employee: employeesAvailable) {
//                boolean toAdd = false;
//                for (EmployeeSkill employeeSkill: employee.getSkills()) {
//                    if (skillsToCover.contains(employeeSkill)) {
//                        skillsToCover.remove(employeeSkill);
//                        toAdd = true;
//                    }
//                }
//                if (toAdd) {
//                    employeeNeeded.add(convertEmployeeToEmployeeDTO(employee));
//                    if (skillsToCover.size() == 0) {
//                        return employeeNeeded;
//                    }
//                }
//            }
//            return employeeNeeded;
            HashMap<Long, Integer> counter = new HashMap<>();
            // every (employee, skill) pair corresponds to one entry in the list employeesAvailable
            for(Employee employee : employeesAvailable){
                counter.merge(employee.getId(),1, Integer::sum);
            }
            for(Long employeeId : counter.keySet()){
                if(counter.get(employeeId) >= skillsToCover.size()){
                    employeeNeeded.add(convertEmployeeToEmployeeDTO(employeeService.findEmployeeById(employeeId)));
                }
            }
            return employeeNeeded;
        } else {
            return convertListEmployeeToListEmployeeDTO(employeesAvailable);
        }
    }

}
