package com.tenniscourts.guests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class GuestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GuestService guestService;

    @InjectMocks
    private GuestController guestController;

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(guestController).build();
    }

    @Test
    public void functionListGuestsShouldReturnAllGuests() throws Exception {
        List<Guest> guestList = List.of(Guest.builder().name("Maria").build(), Guest.builder().name("Matheus").build());
        Mockito.when(guestService.listAllGuests()).thenReturn(guestList);
        mockMvc.perform(get("/guests/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ow.writeValueAsString(guestList)));
    }

    @Test
    public void functionListGuestsShouldReturnAllGuestsWithGivenName() throws Exception {
        Guest expectedGuest = Guest.builder().name("Laura").build();

        Mockito.when(guestService.findByName("Matheus")).thenReturn(List.of(expectedGuest));
        mockMvc.perform(get("/guests/?name=Matheus").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ow.writeValueAsString(List.of(expectedGuest))));
    }

    @Test
    public void functionFindByIdShouldReturnGuestWithMatchedID() throws Exception {
        Guest expectedGuest = Guest.builder().name("Laura").build();
        expectedGuest.setId(123L);

        Mockito.when(guestService.findById(123L)).thenReturn(Optional.of(expectedGuest));
        mockMvc.perform(get("/guests/123").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ow.writeValueAsString(expectedGuest)));
    }

    @Test
    public void functionCreateGuestShouldAddANewGuest() throws Exception {
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setName("Matheus");

        Guest expectedGuest = Guest.builder().name("Matheus").build();
        Mockito.when(guestService.createGuest(guestDTO)).thenReturn(expectedGuest);
        mockMvc.perform(post("/guests/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(guestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void functionPutGuestShouldUpdateGuestWithGivenId() throws Exception {
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setName("Matheus");
        Guest expectedGuest = Guest.builder().name("Matheus").build();

        Mockito.when(guestService.putGuest(any(), any())).thenReturn(Optional.of(expectedGuest));
        mockMvc.perform(put("/guests/1").contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(guestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(ow.writeValueAsString(expectedGuest)));
    }

    @Test
    public void functionDeleteGuestShouldDeleteGuestWithGivenId() throws Exception {
        mockMvc.perform(delete("/guests/1"))
                .andExpect(status().isNoContent());
    }

}
