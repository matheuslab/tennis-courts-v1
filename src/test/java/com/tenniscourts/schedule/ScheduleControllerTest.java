package com.tenniscourts.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleController;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleMapper;
import com.tenniscourts.schedules.ScheduleMapperImpl;
import com.tenniscourts.schedules.ScheduleService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ScheduleMapperImpl.class})
public class ScheduleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @InjectMocks
    private ScheduleController scheduleController;

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
    }

    @Test
    public void functionFindAllSchedulesByGivenTimestamp() throws Exception {
        List<Schedule> scheduleList = List.of(Schedule.builder()
                .startDateTime(LocalDateTime.of(LocalDate.of(2022, 3, 3), LocalTime.of(0, 0))).build());
        Mockito.when(scheduleService.findSchedulesByDates(any(), any())).thenReturn(scheduleMapper.map(scheduleList));
        mockMvc.perform(get("/schedule/?startDate=2022-03-03&endDate=2022-03-03"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":null,\"tennisCourt\":null,\"tennisCourtId\":null,\"startDateTime\":\"2022-03-03T00:00\",\"endDateTime\":null}]"));
    }

    @Test
    public void functionFindScheduleByGivenId() throws Exception {
        Optional<ScheduleDTO> scheduleDTO = Optional.of(new ScheduleDTO());
        scheduleDTO.get().setId(1L);
        Mockito.when(scheduleService.findSchedule(any())).thenReturn(scheduleDTO);
        mockMvc.perform(get("/schedule/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(ow.writeValueAsString(scheduleDTO.get())));
    }

    @Test
    public void functionAddScheduleTennisCourtShouldCreateNewSchedule() throws Exception {
        Schedule schedule = Schedule.builder()
                .startDateTime(LocalDateTime.of(LocalDate.of(2022, 3, 3),
                        LocalTime.of(0, 0))).build();
        ScheduleDTO scheduleDTO = scheduleMapper.map(schedule);
        Mockito.when(scheduleService.addSchedule(any(), any())).thenReturn(scheduleDTO);
        mockMvc.perform(post("/schedule/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tennisCourtId\":1,\"startDateTime\":\"2022-03-10T12:00\"}"))
                .andExpect(status().isCreated());
    }

}
