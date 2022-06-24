package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
//@Transactional
public class ScheduleController {


    private final EmployeeService employeeService;
    private final PetService petService;
    private final ScheduleService scheduleService;
    private final CustomerService customerService;

    public ScheduleController(EmployeeService employeeService, PetService petService, ScheduleService scheduleService, CustomerService customerService) {
        this.employeeService = employeeService;
        this.petService = petService;
        this.scheduleService = scheduleService;
        this.customerService = customerService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertScheduleToScheduleDTO(scheduleService.saveSchedule(convertScheduleDTOToSchedule(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return convertListScheduleToListScheduleDTO(scheduleService.findAllSchedules());
    }

    public ScheduleDTO getSchedule(long id){
        return convertScheduleToScheduleDTO(scheduleService.findScheduleById(id));
    }

    public Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule  = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        List<Pet> pets = new ArrayList<>();
        if(scheduleDTO.getPetIds() != null){
            for(Long petId : scheduleDTO.getPetIds()){
                pets.add(petService.findPetById(petId));
            }
        }
        schedule.setPets(pets);
        List<Employee> employees = new ArrayList<>();
        if(scheduleDTO.getEmployeeIds() != null){
            for(Long employeeId : scheduleDTO.getEmployeeIds()){
                employees.add(employeeService.findEmployeeById(employeeId));
            }
        }
        schedule.setEmployees(employees);
        return schedule;
    }

    public ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        List<Long> petIds = new ArrayList<>();
        if(schedule.getPets() != null){
            for(Pet pet : schedule.getPets()){
                petIds.add(pet.getId());
            }
        }
        scheduleDTO.setPetIds(petIds);
        List<Long> employeeIds = new ArrayList<>();
        if(schedule.getEmployees() != null){
            for(Employee employee : schedule.getEmployees()){
                employeeIds.add(employee.getId());
            }
        }
        scheduleDTO.setEmployeeIds(employeeIds);
        return scheduleDTO;
    }

    public List<ScheduleDTO> convertListScheduleToListScheduleDTO(List<Schedule> schedules) {
        List<ScheduleDTO> result = new ArrayList<>();
        for (Schedule schedule: schedules) {
            result.add(convertScheduleToScheduleDTO(schedule));
        }
        return result;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = scheduleService.findAllSchedulesByPetsId(petId);
        return convertListScheduleToListScheduleDTO(schedules);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = scheduleService.findAllByEmployeesId(employeeId);
        return convertListScheduleToListScheduleDTO(schedules);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = customerService.findCustomerById(customerId);
//        List<Pet> pets = customer.getPets();
//        HashSet<Pet> petsSet = new HashSet<>();
        List<ScheduleDTO> schedules = new ArrayList<>();
//        Set<Long> petIds = new HashSet<>();
        if(customer.getPets() != null){
            for(Pet pet : customer.getPets()){
//                petIds.add(pet.getId());
//                petsSet.add(pet);
                List<Schedule> schedulesThisPet = scheduleService.findAllSchedulesByPetsId(pet.getId());
                schedules.addAll(convertListScheduleToListScheduleDTO(schedulesThisPet));
            }
        }
        return schedules;
    }
}
