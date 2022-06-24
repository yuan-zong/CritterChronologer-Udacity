package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllSchedulesByEmployeesId(Long employeeId);
    List<Schedule> findAllSchedulesByPetsId(Long petId);

//    List<Schedule> findAll(Set<Long> petIds);
}
