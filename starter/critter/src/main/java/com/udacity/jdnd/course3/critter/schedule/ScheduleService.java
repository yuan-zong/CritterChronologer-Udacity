package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule findScheduleById(long id) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isPresent()) {
            Schedule schedule = optionalSchedule.get();
            return schedule;
        } else {
            throw new ScheduleNotFoundException("Schedule Not Found");
        }
    }

    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule saveSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }
    List<Schedule> findAllByEmployeesId(Long employeeId) {
        return scheduleRepository.findAllSchedulesByEmployeesId(employeeId);
    }
    List<Schedule> findAllSchedulesByPetsId(Long petId) {
        return scheduleRepository.findAllSchedulesByPetsId(petId);
    }
//    List<Schedule> findByPetIn(Set<Pet> pets) {
//        return scheduleRepository.findAll(pets);
//    }

}
