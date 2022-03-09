package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RequestMapping("/schedule")
@RestController
public class ScheduleController extends BaseRestController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("Register a schedule")
    @PostMapping("/")
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody @Valid CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO.getTennisCourtId(), createScheduleRequestDTO).getId())).build();
    }

    @ApiOperation("Find schedules by timestamp")
    @GetMapping("/")
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(
            @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @ApiOperation("Retrieve schedule by given id")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<Optional<ScheduleDTO>> findByScheduleId(@PathVariable Long scheduleId) {
        Optional<ScheduleDTO> scheduleDTO = scheduleService.findSchedule(scheduleId);
        if (scheduleDTO.isPresent()) {
            return ResponseEntity.ok(scheduleDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
