package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    @Autowired
    private ScheduleMapper scheduleMapper;

    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
        LocalDateTime startDateTime = createScheduleRequestDTO.getStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);
        List<ScheduleDTO> scheduleDTOList = findSchedulesByTennisCourtId(tennisCourtId);
        if(scheduleDTOList.stream().anyMatch(l -> l.getStartDateTime().isAfter(startDateTime) && l.getStartDateTime().isBefore(endDateTime))){
            throw new AlreadyExistsEntityException("The schedule already exist");
        }

        TennisCourt tennisCourt = tennisCourtRepository.getOne(tennisCourtId);

        Schedule schedule = Schedule.builder()
                .tennisCourt(tennisCourt)
                .startDateTime(startDateTime).endDateTime(endDateTime).build();

        scheduleRepository.save(schedule);

        return scheduleMapper.map(schedule);
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<Schedule> scheduleList = scheduleRepository.
                findAllByStartDateTimeIsGreaterThanEqualAndEndDateTimeIsLessThanEqual(startDate, endDate);
        return scheduleMapper.map(scheduleList);
    }

    public Optional<ScheduleDTO> findSchedule(Long scheduleId) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        return schedule.map(scheduleMapper::map);
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
