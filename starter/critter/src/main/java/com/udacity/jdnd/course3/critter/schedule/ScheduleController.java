package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    public ScheduleController(EmployeeService employeeService, PetService petService, ScheduleService scheduleService) {
        this.employeeService = employeeService;
        this.petService = petService;
        this.scheduleService = scheduleService;
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
        throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        throw new UnsupportedOperationException();
    }
}
